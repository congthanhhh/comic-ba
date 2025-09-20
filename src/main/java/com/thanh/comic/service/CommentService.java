package com.thanh.comic.service;

import com.thanh.comic.dto.request.Comic.CommentRequest;
import com.thanh.comic.dto.response.Comic.CommentResponse;
import com.thanh.comic.dto.response.Comic.RepliesCommentResponse;
import com.thanh.comic.dto.response.Comic.RootCommentResponse;
import com.thanh.comic.entity.Chapter;
import com.thanh.comic.entity.Comic;
import com.thanh.comic.entity.Comment;
import com.thanh.comic.entity.User;
import com.thanh.comic.maper.CommentMapper;
import com.thanh.comic.repository.ChapterRepository;
import com.thanh.comic.repository.ComicRepository;
import com.thanh.comic.repository.CommentRepository;
import com.thanh.comic.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class CommentService {
    CommentRepository commentRepository;
    ComicRepository comicRepository;
    ChapterRepository chapterRepository;
    UserRepository userRepository;
    CommentMapper commentMapper;

    public CommentResponse createComment(CommentRequest request) {
        Comic comic = comicRepository.findById(request.getComicId())
                .orElseThrow(() -> new RuntimeException("Comic not found"));

        Chapter chapter = null;
        if (request.getChapterId() != null) {
            chapter = chapterRepository.findById(request.getChapterId())
                    .orElseThrow(() -> new RuntimeException("Chapter not found"));
        }
        Comment parentComment = null;
        if (request.getParentId() != null) {
            parentComment = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));
        }
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = commentMapper.toComment(request);
        comment.setComic(comic);
        comment.setChapter(chapter);
        comment.setParent(parentComment);
        comment.setCreatedDate(LocalDateTime.now());
        comment.setContent(request.getContent());
        comment.setUser(user);
        comment = commentRepository.save(comment);
        return commentMapper.toCommentResponse(comment);
    }

    public List<CommentResponse> getComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                .map(commentMapper::toCommentResponse)
                .toList();
    }

    public List<CommentResponse> getCommentByComicId2(String comicId) {
        return commentRepository.findByComicId(comicId)
                .stream()
                .map(commentMapper::toCommentResponse)
                .toList();
    }

    public List<CommentResponse> getCommentByComicId(String comicId) {
        List<Comment> comments = commentRepository.findByComicId(comicId);
        Map<Long, CommentResponse> commentMap = comments.stream()
                .map(commentMapper::toCommentResponse)
                .collect(Collectors.toMap(CommentResponse::getCommentId, comment -> comment));

//        Map<Long, CommentResponse> commentMap = new HashMap<>();
//        for (Comment comment : comments) {
//            CommentResponse response = commentMapper.toCommentResponse(comment);
//            commentMap.put(response.getCommentId(), response);
//        }

        List<CommentResponse> rootComments = new ArrayList<>();
        for (CommentResponse comment : commentMap.values()) {
            if (comment.getParentId() == null) {
                rootComments.add(comment);
            } else {
                CommentResponse parent = commentMap.get(comment.getParentId());
                if (parent != null) {
                    parent.getReplies().add(comment);
                }
            }
        }

        return rootComments;
    }

    public List<RootCommentResponse> getRootComments(String comicId) {
        return commentRepository.findByComicId(comicId).stream()
                .filter(comment -> comment.getParent() == null)
                .map(commentMapper::toRootCommentResponse)
                .toList();
    }

    public List<RepliesCommentResponse> getRepliesOfRootComment(Long rootCommentId) {
        return commentRepository.findByParentCommentId(rootCommentId).stream()
                .map(commentMapper::toRepliesCommentResponse)
                .toList();
    }

    public List<RootCommentResponse> getRootCommentsByChapterId(Long chapterId) {
        return commentRepository.findByChapterId(chapterId).stream()
                .filter(comment -> comment.getParent() == null)
                .map(commentMapper::toRootCommentResponse)
                .toList();
    }

}