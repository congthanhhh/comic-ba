package com.thanh.comic.maper;

import com.thanh.comic.dto.request.PermissionRequest;
import com.thanh.comic.dto.response.PermissionResponse;
import com.thanh.comic.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionRequest request);

    PermissionResponse topermissionResponse(Permission permission);
}
