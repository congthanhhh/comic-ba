package com.thanh.comic.dto.response.Comic;

import com.thanh.comic.entity.Genre;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ComicResponse {
    String title;
    String imageUrl;
    String status;
    String description;
    int viewCount;
    String ageRating;
    List<Genre> genres;
    Boolean isActive;
}
