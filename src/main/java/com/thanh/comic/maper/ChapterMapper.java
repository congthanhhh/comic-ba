package com.thanh.comic.maper;

import com.thanh.comic.dto.request.Comic.ChapterRequest;
import com.thanh.comic.dto.response.Comic.ChapterResponse;
import com.thanh.comic.dto.response.Comic.PageChapterResponse;
import com.thanh.comic.entity.Chapter;
import com.thanh.comic.entity.Page;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChapterMapper {

    Chapter toChapter(ChapterRequest request);

    @Mapping(source = "comic.title", target = "comicTitle")
    @Mapping(source = "pages", target = "pages")
    ChapterResponse toChapterResponse(Chapter chapter);
    
    PageChapterResponse toPageChapterResponse(Page page);
}
