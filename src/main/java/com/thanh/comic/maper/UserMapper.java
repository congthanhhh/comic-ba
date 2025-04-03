package com.thanh.comic.maper;

import com.thanh.comic.dto.request.User.UserCreationRequest;
import com.thanh.comic.dto.request.User.UserUpdateRequest;
import com.thanh.comic.dto.response.User.UserResponse;
import com.thanh.comic.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
