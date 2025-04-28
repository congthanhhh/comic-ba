package com.thanh.comic.controller;

import com.thanh.comic.dto.ApiResponse;
import com.thanh.comic.dto.request.Comic.PageRequest;
import com.thanh.comic.dto.response.Comic.PageResponse;
import com.thanh.comic.service.PageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/page")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PageController {

    PageService pageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<List<PageResponse>> uploadPages(@ModelAttribute PageRequest request) {
        List<PageResponse> responses = pageService.uploadPages(request);
        return ApiResponse.<List<PageResponse>>builder()
                .result(responses)
                .build();
    }
    
    @GetMapping("/chapter/{chapterId}")
    public ApiResponse<List<PageResponse>> getPagesByChapter(@PathVariable Long chapterId) {
        List<PageResponse> responses = pageService.getPagesByChapterId(chapterId);
        return ApiResponse.<List<PageResponse>>builder()
                .result(responses)
                .build();
    }
    
    @DeleteMapping("/{pageId}")
    public ApiResponse<Void> deletePage(@PathVariable Long pageId) {
        pageService.deletePage(pageId);
        return ApiResponse.<Void>builder()
                .message("Page deleted successfully")
                .build();
    }
}
