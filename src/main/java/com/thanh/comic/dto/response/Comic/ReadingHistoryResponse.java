package com.thanh.comic.dto.response.Comic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadingHistoryResponse {
    private Long id;
    private String userId;
    private Long chapterId;
    private String chapterTitle;
    private LocalDateTime startedDate;
    private int lastReadPageNumber;
    private LocalDateTime lastViewedDate;
}
