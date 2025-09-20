package com.thanh.comic.repository;

import com.thanh.comic.entity.Comic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComicRepository extends JpaRepository<Comic, String> {

    // JPQL query to find all active comics with pagination
    @Query("SELECT DISTINCT c FROM Comic c LEFT JOIN c.chapters ch WHERE c.isActive = true " +
            "GROUP BY c ORDER BY MAX(ch.releaseDate) DESC")
    Page<Comic> findActiveComicsOrderByLatestChapter(Pageable pageable);
    
    // Find comics ordered by release date (newest first)
    @Query("SELECT c FROM Comic c WHERE c.isActive = true ORDER BY c.releaseDate ")
    Page<Comic> findActiveComicsOrderByReleaseDate(Pageable pageable);
    
    // Find comics ordered by view count (highest first)
    @Query("SELECT c FROM Comic c WHERE c.isActive = true ORDER BY c.viewCount DESC")
    Page<Comic> findActiveComicsOrderByViewCount(Pageable pageable);

    // Search comics by title (case insensitive)
    @Query("SELECT c FROM Comic c WHERE c.isActive = true AND LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%')) ORDER BY c.title ASC")
    Page<Comic> findActiveComicsByTitleContaining(@Param("title") String title, Pageable pageable);

    // Search comics by title without pagination
    @Query("SELECT c FROM Comic c WHERE c.isActive = true AND LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%')) ORDER BY c.title ASC")
    List<Comic> findActiveComicsByTitleContaining(@Param("title") String title);
}
