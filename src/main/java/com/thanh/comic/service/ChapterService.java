package com.thanh.comic.service;

import com.thanh.comic.dto.request.Comic.ChapterRequest;
import com.thanh.comic.dto.response.Comic.ChapterResponse;
import com.thanh.comic.entity.Chapter;
import com.thanh.comic.entity.Page;
import com.thanh.comic.entity.ReadingHistory;
import com.thanh.comic.entity.User;
import com.thanh.comic.exception.AppException;
import com.thanh.comic.exception.ErrorCode;
import com.thanh.comic.maper.ChapterMapper;
import com.thanh.comic.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
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
    ReadingHistoryRepository readingHistoryRepository;
    UserRepository userRepository;

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

        List<Chapter> chapters = chapterRepository.findByComicIdAndIsActiveOrderByChapterNumberDesc(comicId, true);
        return chapters.stream().map(chapterMapper::toChapterResponse).toList();
    }

    @Transactional
    public Chapter getChapterAndIncrementViewCount(Long chapterId) {

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new AppException(ErrorCode.CHAPTER_NOT_FOUND));
        if (username != null) {
            var readingHistory = readingHistoryRepository.findByUsernameAndChapterId(username, chapterId)
                    .orElse(null);
            boolean incrementViewCount = false;
            LocalDateTime now = LocalDateTime.now();

            if (readingHistory == null) {
                incrementViewCount = true;
                readingHistory = new ReadingHistory();
//                readingHistory.setUser(userReading);
                readingHistory.setChapter(chapter);
                readingHistory.setStartedDate(now);
                readingHistory.setLastViewedDate(now);
            } else {
                LocalDateTime lastViewed = readingHistory.getLastViewedDate();
                if (lastViewed == null || lastViewed.isBefore(now.minusMinutes(5))) {
                    incrementViewCount = true;
                    readingHistory.setLastViewedDate(now);
                }
            }
            if (incrementViewCount) {
                chapter.setViewCount(chapter.getViewCount() + 1);
                chapterRepository.save(chapter);
            }
            readingHistoryRepository.save(readingHistory);
        } else {
            chapter.setViewCount(chapter.getViewCount() + 1);
            chapterRepository.save(chapter);
        }
        return chapter;
    }
}
