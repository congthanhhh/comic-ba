package com.thanh.comic.repository;

import com.thanh.comic.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByParentCommentId(Long parentId);
    List<Comment> findByComicId(String comicId);
    List<Comment> findByComicId
}
