package com.thanh.comic.maper;

import com.thanh.comic.dto.request.Comic.ChapterRequest;
import com.thanh.comic.dto.request.Comic.PageRequest;
import com.thanh.comic.dto.response.Comic.ChapterResponse;
import com.thanh.comic.dto.response.Comic.PageResponse;
import com.thanh.comic.entity.Chapter;
import com.thanh.comic.entity.Page;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PageMapper {

    Chapter toChapter(ChapterRequest request);

    @Mapping(source = "chapter.chapterNumber", target = "chapterNumber")
    @Mapping(source = "chapter.comic.title", target = "titleComic")
    PageResponse toPageResponse(Page page);

    Page toPage(PageRequest request);
}
