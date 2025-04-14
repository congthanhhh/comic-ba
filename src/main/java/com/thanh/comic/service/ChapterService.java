package com.thanh.comic.service;

import com.thanh.comic.dto.request.Comic.ChapterRequest;
import com.thanh.comic.dto.response.Comic.ChapterResponse;
import com.thanh.comic.entity.Chapter;
import com.thanh.comic.entity.Page;
import com.thanh.comic.maper.ChapterMapper;
import com.thanh.comic.repository.ChapterRepository;
import com.thanh.comic.repository.ComicRepository;
import com.thanh.comic.repository.PageRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ChapterService {

    ChapterRepository chapterRepository;
    ComicRepository comicRepository;
    PageRepository pageRepository;
    ChapterMapper chapterMapper;
    CloudinaryService cloudinaryService;

    @Transactional
    public ChapterResponse createChapter(ChapterRequest request) {
        var comic = comicRepository.findById(request.getComicId())
                .orElseThrow(() -> new RuntimeException("Comic not found"));
        
        if(chapterRepository.existsByChapterNumberAndComicId
                (request.getChapterNumber(), request.getComicId())) {
            throw new RuntimeException("Chapter already exists");
        }
        
        Chapter chapter = chapterMapper.toChapter(request);
        chapter.setComic(comic);
        
        if (request.getReleaseDate() == null) {
            chapter.setReleaseDate(LocalDateTime.now());
        } else {
            chapter.setReleaseDate(request.getReleaseDate());
        }
        
        chapter.setViewCount(0);
        chapter.setIsActive(true);
        
        // Save chapter first to get its ID
        Chapter savedChapter = chapterRepository.save(chapter);
        
        // Handle images if any
        if (!CollectionUtils.isEmpty(request.getImages())) {
            List<Page> pages = new ArrayList<>();
            int pageNumber = 1;
            
            for (MultipartFile image : request.getImages()) {
                try {
                    // Upload image to Cloudinary
                    String imageUrl = cloudinaryService.uploadImage(image, "comic_web/chapter/");
                    
                    // Create new page
                    Page page = new Page();
                    page.setPageNumber(pageNumber++);
                    page.setImageUrl(imageUrl);
                    page.setChapter(savedChapter);
                    
                    // Save page
                    pages.add(pageRepository.save(page));
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload image: " + e.getMessage(), e);
                }
            }
            
            savedChapter.setPages(pages);
        }
        
        return chapterMapper.toChapterResponse(savedChapter);
    }
}
