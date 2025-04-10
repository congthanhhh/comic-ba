package com.thanh.comic.dto.request.Comic;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FollowRequest {
    String userId;
    String comicId;
}
