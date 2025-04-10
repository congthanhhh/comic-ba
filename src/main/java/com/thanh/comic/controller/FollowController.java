package com.thanh.comic.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thanh.comic.dto.ApiResponse;
import com.thanh.comic.dto.request.Comic.ComicRequest;
import com.thanh.comic.dto.request.Comic.FollowRequest;
import com.thanh.comic.dto.response.Comic.ComicResponse;
import com.thanh.comic.dto.response.Comic.FollowResponse;
import com.thanh.comic.entity.Follow;
import com.thanh.comic.service.ComicService;
import com.thanh.comic.service.FollowService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/follow")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FollowController {

    FollowService followService;

    @PostMapping
    ResponseEntity<FollowResponse> createFollow(@RequestBody FollowRequest request) {
        return ResponseEntity.ok(followService.createFollow(request));
    }

    @GetMapping
    ResponseEntity<List<FollowResponse>> getAll() {
        return ResponseEntity.ok(followService.getAll());
    }

    @GetMapping("/{userId}")
    ResponseEntity<List<FollowResponse>> getAllByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(followService.getAllByUserId(userId));
    }

}
