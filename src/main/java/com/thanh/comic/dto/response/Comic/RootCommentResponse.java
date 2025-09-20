package com.thanh.comic.dto.response.Comic;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RootCommentResponse {
    Long commentId;
    String content;
    String createdDate;
    String userName;
    String comicTitle;
    String chapterNumber;
    Long parentId;
}
