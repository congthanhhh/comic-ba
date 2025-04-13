package com.thanh.comic.controller;


import com.thanh.comic.dto.ApiResponse;
import com.thanh.comic.dto.request.Comic.ChapterRequest;
import com.thanh.comic.dto.response.Comic.ChapterResponse;
import com.thanh.comic.service.ChapterService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chapter")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChapterController {

    ChapterService chapterService;

    @PostMapping
    ApiResponse<ChapterResponse> createChapter(@RequestBody ChapterRequest request) {
        return ApiResponse.<ChapterResponse>builder()
                .result(chapterService.createChapter(request))
                .build();
    }
}
