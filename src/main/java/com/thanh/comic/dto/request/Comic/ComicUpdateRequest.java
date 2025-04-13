package com.thanh.comic.dto.request.Comic;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ComicUpdateRequest {
    String title;
    String description;
    MultipartFile file;
    List<Long> genres;
}
