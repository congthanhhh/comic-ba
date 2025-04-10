package com.thanh.comic.dto.response.Comic;

import com.thanh.comic.entity.Comic;
import com.thanh.comic.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FollowResponse {
    Comic comic;
    User user;
}
