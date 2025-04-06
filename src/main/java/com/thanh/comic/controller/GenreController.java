package com.thanh.comic.controller;

import com.thanh.comic.dto.ApiResponse;
import com.thanh.comic.dto.request.Comic.GenreRequest;
import com.thanh.comic.dto.response.Comic.GenreResponse;
import com.thanh.comic.service.GenreService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/genre")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GenreController {
    GenreService genreService;

    @PostMapping
    ApiResponse<GenreResponse> createGenre(@RequestBody GenreRequest request) {
        return ApiResponse.<GenreResponse>builder()
                .result(genreService.createGenre(request))
                .build();
    }

    @PutMapping("/{genreId}")
    ApiResponse<GenreResponse> updateGenre(@PathVariable Long genreId, @RequestBody GenreRequest request) {
        return ApiResponse.<GenreResponse>builder()
                .result(genreService.updateGenre(genreId, request))
                .build();
    }

    @DeleteMapping("/{genreId}")
    ApiResponse<Void> deleteGenre(@PathVariable Long genreId) {
        genreService.deleteGenre(genreId);
        return ApiResponse.<Void>builder()
                .message("Delete genre successfully")
                .build();
    }

    @GetMapping("/{genreId}")
    ApiResponse<GenreResponse> getGenre(@PathVariable Long genreId) {
        return ApiResponse.<GenreResponse>builder()
                .result(genreService.getGenre(genreId))
                .build();
    }

    @GetMapping
    ApiResponse<List<GenreResponse>> getAllGenres() {
        return ApiResponse.<List<GenreResponse>>builder()
                .result(genreService.getAllGenres())
                .build();
    }
}
