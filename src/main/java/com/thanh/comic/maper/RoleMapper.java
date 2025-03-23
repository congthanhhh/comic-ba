package com.thanh.comic.maper;

import com.thanh.comic.dto.request.RoleRequest;
import com.thanh.comic.dto.response.RoleResponse;
import com.thanh.comic.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
