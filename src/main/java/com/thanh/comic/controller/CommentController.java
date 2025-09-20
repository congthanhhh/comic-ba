package com.thanh.comic.controller;

import com.thanh.comic.dto.ApiResponse;
import com.thanh.comic.dto.request.Comic.CommentRequest;
import com.thanh.comic.dto.response.Comic.CommentResponse;
import com.thanh.comic.dto.response.Comic.RepliesCommentResponse;
import com.thanh.comic.dto.response.Comic.RootCommentResponse;
import com.thanh.comic.service.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {

    CommentService commentService;

    @PostMapping
    ApiResponse<CommentResponse> create(@RequestBody CommentRequest request) {
        return ApiResponse.<CommentResponse>builder()
                .result(commentService.createComment(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<CommentResponse>> getComments() {
        return ApiResponse.<List<CommentResponse>>builder()
                .result(commentService.getComments())
                .build();
    }

    @GetMapping("/comic/{comicId}")
    ApiResponse<List<CommentResponse>> getCommentsByComicId(@PathVariable String comicId) {
        return ApiResponse.<List<CommentResponse>>builder()
                .result(commentService.getCommentByComicId(comicId))
                .build();
    }

    @GetMapping("/root/{comicId}")
    ApiResponse<List<RootCommentResponse>> getRootComments(@PathVariable String comicId) {
        return ApiResponse.<List<RootCommentResponse>>builder()
                .result(commentService.getRootComments(comicId))
                .build();
    }

    @GetMapping("/root-chapter/{chapterId}")
    ApiResponse<List<RootCommentResponse>> getRootChapter(@PathVariable Long chapterId) {
        return ApiResponse.<List<RootCommentResponse>>builder()
                .result(commentService.getRootCommentsByChapterId(chapterId))
                .build();
    }

    @GetMapping("/replies/{commentId}")
    ApiResponse<List<RepliesCommentResponse>> getRepliesComments(@PathVariable Long commentId) {
        return ApiResponse.<List<RepliesCommentResponse>>builder()
                .result(commentService.getRepliesOfRootComment(commentId))
                .build();
    }

}
