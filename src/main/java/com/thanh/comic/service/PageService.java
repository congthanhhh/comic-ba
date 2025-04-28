package com.thanh.comic.service;

import com.thanh.comic.dto.request.Comic.PageRequest;
import com.thanh.comic.dto.response.Comic.PageResponse;
import com.thanh.comic.entity.Chapter;
import com.thanh.comic.entity.Page;
import com.thanh.comic.exception.AppException;
import com.thanh.comic.exception.ErrorCode;
import com.thanh.comic.maper.PageMapper;
import com.thanh.comic.repository.ChapterRepository;
import com.thanh.comic.repository.PageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PageService {

    PageRepository pageRepository;
    PageMapper pageMapper;
    CloudinaryService cloudinaryService;
    ChapterRepository chapterRepository;

    @Transactional
    public List<PageResponse> uploadPages(PageRequest request) {
        Chapter chapter = chapterRepository.findById(request.getChapterId())
                .orElseThrow(() -> new AppException(ErrorCode.CHAPTER_NOT_FOUND));
        int maxPageNumber = pageRepository.findMaxPageNumberByChapterId(request.getChapterId()).orElse(0);
        
        List<Page> uploadedPages = new ArrayList<>();
        int pageNumber = maxPageNumber + 1;
        
        for (MultipartFile image : request.getImages()) {
            try {
                String imageUrl = cloudinaryService.uploadImage(image, "comic_web/chapter/");
                
                Page page = Page.builder()
                        .pageNumber(pageNumber++)
                        .imageUrl(imageUrl)
                        .chapter(chapter)
                        .build();
                
                uploadedPages.add(pageRepository.save(page));
            } catch (IOException e) {
                throw new AppException(ErrorCode.FILE_PROCESSING_ERROR);
            }
        }
        
        return uploadedPages.stream()
                .map(pageMapper::toPageResponse)
                .collect(Collectors.toList());
    }

    public List<PageResponse> getPagesByChapterId(Long chapterId) {
        if (!chapterRepository.existsById(chapterId)) {
            throw new AppException(ErrorCode.CHAPTER_NOT_FOUND);
        }
        
        List<Page> pages = pageRepository.findByChapterIdOrderByPageNumber(chapterId);
        
        return pages.stream()
                .map(pageMapper::toPageResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePage(Long pageId) {
        Page page = pageRepository.findById(pageId)
                .orElseThrow(() -> new AppException(ErrorCode.PAGE_NOT_FOUND));

        if (page.getImageUrl() != null && !page.getImageUrl().isEmpty()) {
            cloudinaryService.deleteImage(page.getImageUrl());
        }
        
        pageRepository.delete(page);

        List<Page> remainingPages = pageRepository.findByChapterIdOrderByPageNumber(page.getChapter().getId());
        int pageNumber = 1;
        
        for (Page remainingPage : remainingPages) {
            remainingPage.setPageNumber(pageNumber++);
            pageRepository.save(remainingPage);
        }
    }

}
