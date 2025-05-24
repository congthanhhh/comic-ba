package com.thanh.comic.controller;


import com.thanh.comic.dto.ApiResponse;
import com.thanh.comic.dto.request.Comic.ChapterRequest;
import com.thanh.comic.dto.response.Comic.ChapterResponse;
import com.thanh.comic.dto.response.Comic.ReadingHistoryResponse;
import com.thanh.comic.entity.Chapter;
import com.thanh.comic.service.ChapterService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chapter")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChapterController {

    ChapterService chapterService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<ChapterResponse> createChapter(@ModelAttribute ChapterRequest request) {
        return ApiResponse.<ChapterResponse>builder()
                .result(chapterService.createChapter(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<ChapterResponse>> getAllChapters() {
        return ApiResponse.<List<ChapterResponse>>builder()
                .result(chapterService.getAllChapters())
                .build();
    }

    @GetMapping("/comic/{comicId}")
    ApiResponse<List<ChapterResponse>> getAllChaptersByComic(@PathVariable String comicId) {
        return ApiResponse.<List<ChapterResponse>>builder()
                .result(chapterService.getActiveChaptersByComicId(comicId))
                .build();
    }

    @GetMapping("top2/{comicId}")
    ApiResponse<List<ChapterResponse>> getTop2Chapters(@PathVariable String comicId) {
        return ApiResponse.<List<ChapterResponse>>builder()
                .result(chapterService.getTop2ChaptersByComicId(comicId))
                .build();
    }

    @PostMapping("increase-view/{chapterId}")
    ApiResponse<ReadingHistoryResponse> increaseView(@PathVariable Long chapterId) {
        return ApiResponse.<ReadingHistoryResponse>builder()
                .result(chapterService.incrementViewCount(chapterId))
                .build();
    }
}

