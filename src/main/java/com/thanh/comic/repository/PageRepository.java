package com.thanh.comic.repository;


import com.thanh.comic.entity.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PageRepository extends JpaRepository<Page, Long> {
    List<Page> findByChapterIdOrderByPageNumber(Long chapterId);

    @Query("SELECT MAX(p.pageNumber) FROM Page p WHERE p.chapter.id = :chapterId")
    Optional<Integer> findMaxPageNumberByChapterId(@Param("chapterId") Long chapterId);
}
