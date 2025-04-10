package com.thanh.comic.dto.request.Comic;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ComicRequest {
    String title;
    MultipartFile file;
    int viewCount;
    List<Long> genres;
    String description;
}
