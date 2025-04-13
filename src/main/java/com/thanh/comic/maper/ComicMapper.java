package com.thanh.comic.maper;

import com.thanh.comic.dto.request.Comic.ComicRequest;
import com.thanh.comic.dto.request.Comic.ComicUpdateRequest;
import com.thanh.comic.dto.response.Comic.ComicResponse;
import com.thanh.comic.entity.Comic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ComicMapper {

    @Mappings({
            @Mapping(target = "imageUrl", ignore = true),
            @Mapping(target = "genres", ignore = true),
    })
    Comic toComic(ComicRequest request);

    ComicResponse toComicResponse(Comic comic);

    @Mappings({
            @Mapping(target = "imageUrl", ignore = true),
            @Mapping(target = "genres", ignore = true),
            @Mapping(target = "isActive", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "chapters", ignore = true),
            @Mapping(target = "viewCount", ignore = true)
    })
    void updateComicFromRequest(ComicUpdateRequest request, @MappingTarget Comic comic);
}
