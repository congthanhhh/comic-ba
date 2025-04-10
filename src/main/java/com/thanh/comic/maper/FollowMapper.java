package com.thanh.comic.maper;

import com.thanh.comic.dto.request.Comic.ComicRequest;
import com.thanh.comic.dto.response.Comic.ComicResponse;
import com.thanh.comic.dto.response.Comic.FollowResponse;
import com.thanh.comic.entity.Comic;
import com.thanh.comic.entity.Follow;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface FollowMapper {

    FollowResponse toFollowResponse(Follow follow);
}
