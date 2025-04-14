package com.thanh.comic.dto.response.Comic;

import com.thanh.comic.entity.Comic;
import com.thanh.comic.entity.Page;
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
    int chapterNumber;
    String title;
    LocalDateTime releaseDate;
    String summary;
    int viewCount;
    boolean isActive;
    List<PageResponse> pages; // Changed from List<Page> to List<PageResponse>
    String comicTitle;
}
