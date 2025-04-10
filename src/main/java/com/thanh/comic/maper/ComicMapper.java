package com.thanh.comic.maper;

import com.thanh.comic.dto.request.Comic.ComicRequest;
import com.thanh.comic.dto.response.Comic.ComicResponse;
import com.thanh.comic.entity.Comic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ComicMapper {

    @Mappings({
            @Mapping(target = "imageUrl", ignore = true),
            @Mapping(target = "genres", ignore = true),
    })
    Comic toComic(ComicRequest request);

    ComicResponse toComicResponse(Comic comic);
}
