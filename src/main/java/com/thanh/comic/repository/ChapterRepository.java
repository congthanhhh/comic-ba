package com.thanh.comic.repository;

import com.thanh.comic.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    boolean existsByChapterNumberAndComicId(int chapterNumber, String comicId);
}
