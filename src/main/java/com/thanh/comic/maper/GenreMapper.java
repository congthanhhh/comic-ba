package com.thanh.comic.maper;

import com.thanh.comic.dto.request.Comic.GenreRequest;
import com.thanh.comic.dto.response.Comic.GenreResponse;
import com.thanh.comic.entity.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    Genre toGenre(GenreRequest request);

    GenreResponse toGenreResponse(Genre genre);
}
