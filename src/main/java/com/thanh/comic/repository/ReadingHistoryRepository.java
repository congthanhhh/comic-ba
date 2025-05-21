package com.thanh.comic.repository;

import com.thanh.comic.entity.ReadingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReadingHistoryRepository extends JpaRepository<ReadingHistory, Long> {

    @Query("SELECT rh FROM ReadingHistory rh WHERE rh.userId = :userId AND rh.chapter.id = :chapterId")
    Optional<ReadingHistory> findByUserIdAndChapterId(String userId, Long chapterId);
}
