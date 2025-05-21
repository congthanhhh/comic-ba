package com.thanh.comic.maper;

import com.thanh.comic.dto.response.Comic.ReadingHistoryResponse;
import com.thanh.comic.entity.ReadingHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReadingHistoryMapper {
    
    @Mapping(source = "chapter.id", target = "chapterId")
    @Mapping(source = "chapter.title", target = "chapterTitle")
    ReadingHistoryResponse toReadingHistoryResponse(ReadingHistory readingHistory);
}
