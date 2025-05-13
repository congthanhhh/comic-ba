package com.thanh.comic.repository;

import com.thanh.comic.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Chapter c " +
            "WHERE c.chapterNumber = :chapterNumber AND c.comic.id = :comicId")
    boolean existsByChapterNumberAndComicId(@Param("chapterNumber") int chapterNumber,
                                            @Param("comicId") String comicId);


    List<Chapter> findByComicIdAndIsActiveOrderByChapterNumberDesc(String comicId, Boolean isActive);

    // Count active chapters for a specific comic
    long countByComicIdAndIsActive(String comicId, Boolean isActive);

    // Sum viewCount of all chapters for a specific comic
    @Query("SELECT COALESCE(SUM(c.viewCount), 0) FROM Chapter c " +
            "WHERE c.comic.id = :comicId AND c.isActive = :isActive")
    int sumViewCountByComicIdAndIsActive(@Param("comicId") String comicId, @Param("isActive") Boolean isActive);


    @Query(value = "SELECT c.*, TIMESTAMPDIFF(HOUR, c.release_date, NOW()) AS hours_ago " +
            "FROM chapter c WHERE c.comic_id = :comicId ORDER BY c.release_date DESC", nativeQuery = true)
    List<Map<String, Object>> findChaptersWithHoursAgo(@Param("comicId") Long comicId);
}
