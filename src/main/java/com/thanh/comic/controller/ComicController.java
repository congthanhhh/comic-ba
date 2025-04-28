package com.thanh.comic.controller;

import com.thanh.comic.dto.ApiResponse;
import com.thanh.comic.dto.request.Comic.ComicRequest;
import com.thanh.comic.dto.request.Comic.ComicUpdateRequest;
import com.thanh.comic.dto.response.Comic.ComicResponse;
import com.thanh.comic.dto.response.Comic.PaginatedResponse;
import com.thanh.comic.service.ComicService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/manga")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ComicController {

    ComicService comicService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ComicResponse> createComic(@ModelAttribute ComicRequest request) throws IOException {
        ComicResponse response = comicService.createComic(request);
        return ApiResponse.<ComicResponse>builder()
                .result(response)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ComicResponse> getComic(@PathVariable String id) {
        ComicResponse response = comicService.getComicById(id);
        return ApiResponse.<ComicResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Comic retrieved successfully")
                .result(response)
                .build();
    }

    @GetMapping
    public ApiResponse<List<ComicResponse>> getAllComics() {
        List<ComicResponse> comics = comicService.getAllComics();
        return ApiResponse.<List<ComicResponse>>builder()
                .result(comics)
                .build();
    }

    @GetMapping("/paginated")
    public ApiResponse<PaginatedResponse<ComicResponse>> getComicsPaginated(
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize) {
        PaginatedResponse<ComicResponse> result = comicService.getComicsPaginated(page, pageSize);
        return ApiResponse.<PaginatedResponse<ComicResponse>>builder()
                .result(result)
                .build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ComicResponse> updateComic(
            @PathVariable String id,
            @ModelAttribute ComicUpdateRequest request) throws IOException {
        ComicResponse response = comicService.updateComic(id, request);
        return ApiResponse.<ComicResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Comic updated successfully")
                .result(response)
                .build();
    }

    @PutMapping("/active/{id}")
    public ApiResponse<Void> updateActive(@PathVariable String id) {
        comicService.updateActive(id);
        return ApiResponse.<Void>builder()
                .message("Comic active is false")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteComic(@PathVariable String id) {
        comicService.deleteComic(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Comic deleted successfully")
                .build();
    }
}

