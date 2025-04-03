package com.thanh.comic.dto.response.User;

import com.thanh.comic.dto.response.Role.RoleResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    Boolean noPassword;
    String email;
    Set<RoleResponse> roles;
}
