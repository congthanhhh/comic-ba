package com.thanh.comic.repository;

import com.thanh.comic.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByUserIdAndComicId(String userId, String comicId);
    void deleteByUserIdAndComicId(String userId, String comicId);
    List<Follow> findAllByUserId(String userId);
}
