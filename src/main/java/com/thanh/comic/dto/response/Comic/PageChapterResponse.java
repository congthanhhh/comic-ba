package com.thanh.comic.dto.response.Comic;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageChapterResponse {
    int pageNumber;
    String imageUrl;
}
