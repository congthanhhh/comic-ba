package com.thanh.comic.service;

import com.thanh.comic.dto.request.Comic.ChapterRequest;
import com.thanh.comic.dto.response.Comic.ChapterResponse;
import com.thanh.comic.entity.Chapter;
import com.thanh.comic.maper.ChapterMapper;
import com.thanh.comic.repository.ChapterRepository;
import com.thanh.comic.repository.ComicRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ChapterService {

    ChapterRepository chapterRepository;
    ComicRepository comicRepository;
    ChapterMapper chapterMapper;

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
        return chapterMapper.toChapterResponse(chapterRepository.save(chapter));
    }



    public Object updateChapter(Object request) {
        // Implement the logic to update a chapter
        return null;
    }

    public Object getAllChapters() {
        // Implement the logic to get all chapters
        return null;
    }

    public Void deleteChapter(String chapterId) {
        // Implement the logic to delete a chapter
        return null;
    }


}
