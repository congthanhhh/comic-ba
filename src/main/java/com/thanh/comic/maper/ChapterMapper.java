package com.thanh.comic.maper;

import com.thanh.comic.dto.request.Comic.ChapterRequest;
import com.thanh.comic.dto.response.Comic.ChapterResponse;
import com.thanh.comic.entity.Chapter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChapterMapper {

    Chapter toChapter(ChapterRequest request);

    @Mapping(source = "comic.title", target = "comicTitle")
    ChapterResponse toChapterResponse(Chapter chapter);
}
