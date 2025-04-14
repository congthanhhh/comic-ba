package com.thanh.comic.repository;

import com.thanh.comic.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Chapter c " +
           "WHERE c.chapterNumber = :chapterNumber AND c.comic.id = :comicId")
    boolean existsByChapterNumberAndComicId(@Param("chapterNumber") int chapterNumber, 
                                           @Param("comicId") String comicId);
}
