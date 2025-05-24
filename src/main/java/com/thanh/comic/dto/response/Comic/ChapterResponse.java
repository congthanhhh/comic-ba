package com.thanh.comic.dto.response.Comic;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChapterResponse {
    Long id;
    int chapterNumber;
    String title;
    LocalDateTime releaseDate;
    String summary;
    int viewCount;
    boolean isActive;
    List<PageChapterResponse> pages;
    String comicTitle;
}
