package com.thanh.comic.service;

import com.thanh.comic.dto.request.Comic.ChapterRequest;
import com.thanh.comic.dto.response.Comic.ChapterResponse;
import com.thanh.comic.dto.response.Comic.ReadingHistoryResponse;
import com.thanh.comic.entity.Chapter;
import com.thanh.comic.entity.Page;
import com.thanh.comic.entity.ReadingHistory;
import com.thanh.comic.entity.User;
import com.thanh.comic.exception.AppException;
import com.thanh.comic.exception.ErrorCode;
import com.thanh.comic.maper.ChapterMapper;
import com.thanh.comic.maper.ReadingHistoryMapper;
import com.thanh.comic.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ChapterService {

    ChapterRepository chapterRepository;
    ComicRepository comicRepository;
    PageRepository pageRepository;
    ChapterMapper chapterMapper;
    CloudinaryService cloudinaryService;
    ReadingHistoryRepository readingHistoryRepository;
    UserRepository userRepository;
    ReadingHistoryMapper readingHistoryMapper;

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

        Chapter savedChapter = chapterRepository.save(chapter);

        if (!CollectionUtils.isEmpty(request.getImages())) {
            List<Page> pages = new ArrayList<>();
            int pageNumber = 1;

            for (MultipartFile image : request.getImages()) {
                try {
                    String imageUrl = cloudinaryService.uploadImage(image, "comic_web/chapter/");

                    Page page = new Page();
                    page.setPageNumber(pageNumber++);
                    page.setImageUrl(imageUrl);
                    page.setChapter(savedChapter);

                    pages.add(pageRepository.save(page));
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload image: " + e.getMessage(), e);
                }
            }

            savedChapter.setPages(pages);
        }

        return chapterMapper.toChapterResponse(savedChapter);
    }

    public List<ChapterResponse> getAllChapters() {
        List<Chapter> chapters = chapterRepository.findAll();
        return chapters.stream().map(chapterMapper::toChapterResponse).toList();
    }

    public List<ChapterResponse> getActiveChaptersByComicId(String comicId) {
        comicRepository.findById(comicId)
                .orElseThrow(() -> new RuntimeException("Comic not found"));

        List<Chapter> chapters =
                chapterRepository.findByComicIdAndIsActiveOrderByReleaseDateDesc(comicId, true);
        return chapters.stream().map(chapterMapper::toChapterResponse).toList();
    }

    public List<ChapterResponse> getTop2ChaptersByComicId(String comicId) {
        comicRepository.findById(comicId)
                .orElseThrow(() -> new RuntimeException("Comic not found"));

        List<Chapter> chapters =
                chapterRepository.findByTop2Chapters(comicId, true);
        return chapters.stream().map(chapterMapper::toChapterResponse).toList();
    }

    @Transactional
    public ReadingHistoryResponse incrementViewCount(Long chapterId) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new AppException(ErrorCode.CHAPTER_NOT_FOUND));

        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User userId = userRepository.findByUsername(name).orElse(null);
        
        ReadingHistory history = null;

        if (userId != null) {
            Optional<ReadingHistory> existingHistoryOpt = readingHistoryRepository
                    .findByUserIdAndChapterId(userId.getId(), chapterId);
            
            if (existingHistoryOpt.isPresent()) {
                history = existingHistoryOpt.get();
                LocalDateTime lastViewedDate = history.getLastViewedDate();
                
                if (lastViewedDate == null || 
                    lastViewedDate.plusMinutes(5).isBefore(LocalDateTime.now())) {
                    chapter.setViewCount(chapter.getViewCount() + 1);
                }

                history.setLastViewedDate(LocalDateTime.now());
                history = readingHistoryRepository.save(history);
            } else {
                history = new ReadingHistory();
                history.setUserId(userId.getId());
                history.setChapter(chapter);
                history.setStartedDate(LocalDateTime.now());
                history.setLastViewedDate(LocalDateTime.now());
                history.setLastReadPageNumber(1);
                history = readingHistoryRepository.save(history);

                chapter.setViewCount(chapter.getViewCount() + 1);
            }
        } else {
            chapter.setViewCount(chapter.getViewCount() + 1);
            // For anonymous users, create a temporary ReadingHistory object
            // This won't be saved to the database but will be used to return a response
            history = new ReadingHistory();
            history.setChapter(chapter);
            history.setLastViewedDate(LocalDateTime.now());
            history.setLastReadPageNumber(1);
        }

        chapterRepository.save(chapter);
        return readingHistoryMapper.toReadingHistoryResponse(history);
    }
}
