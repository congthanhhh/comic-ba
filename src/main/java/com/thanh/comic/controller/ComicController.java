package com.thanh.comic.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thanh.comic.dto.ApiResponse;
import com.thanh.comic.dto.request.Comic.ComicRequest;
import com.thanh.comic.dto.response.Comic.ComicResponse;
import com.thanh.comic.service.ComicService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/manga")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ComicController {

    ComicService comicService;

    @PostMapping
    ApiResponse<ComicResponse> createManga(@RequestParam("title")String  title,
                                           @RequestParam("file") MultipartFile file,
                                           @RequestParam("viewCount") int viewCount,
                                           @RequestParam("genres") String genres,
                                           @RequestParam("description") String description) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        List<Long> genreList = objectMapper.readValue(genres, new TypeReference<List<Long>>() {});
        ComicRequest request = new ComicRequest(title, file, viewCount, genreList, description);
        return ApiResponse.<ComicResponse>builder()
                .result(comicService.create(request))
                .build();
    }
}
