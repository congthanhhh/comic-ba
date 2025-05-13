package com.thanh.comic.repository;

import com.thanh.comic.entity.Comic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ComicRepository extends JpaRepository<Comic, String> {

    // JPQL query to find all active comics with pagination
    @Query("SELECT DISTINCT c FROM Comic c LEFT JOIN c.chapters ch WHERE c.isActive = true " +
            "GROUP BY c ORDER BY MAX(ch.releaseDate) DESC")
    Page<Comic> findActiveComicsOrderByLatestChapter(Pageable pageable);
    
    // Find comics ordered by release date (newest first)
    @Query("SELECT c FROM Comic c WHERE c.isActive = true ORDER BY c.releaseDate DESC")
    Page<Comic> findActiveComicsOrderByReleaseDate(Pageable pageable);
}
