package com.thanh.comic.maper;

import com.thanh.comic.dto.request.Comic.CommentRequest;
import com.thanh.comic.dto.response.Comic.CommentResponse;
import com.thanh.comic.dto.response.Comic.RepliesCommentResponse;
import com.thanh.comic.dto.response.Comic.RootCommentResponse;
import com.thanh.comic.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment toComment(CommentRequest request);

    @Mappings({
            @Mapping(target = "userName", source = "user.username"),
            @Mapping(target = "comicTitle", source = "comic.title"),
            @Mapping(target = "parentId", source = "parent.commentId"),
            @Mapping(target = "chapterNumber", source = "chapter.chapterNumber"),
    })
    CommentResponse toCommentResponse(Comment comment);

    @Mappings({
            @Mapping(target = "userName", source = "user.username"),
            @Mapping(target = "comicTitle", source = "comic.title"),
            @Mapping(target = "chapterNumber", source = "chapter.chapterNumber"),
    })
    RootCommentResponse toRootCommentResponse(Comment comment);

    @Mappings({
            @Mapping(target = "userName", source = "user.username"),
            @Mapping(target = "comicTitle", source = "comic.title"),
            @Mapping(target = "parentId", source = "parent.commentId"),
            @Mapping(target = "chapterNumber", source = "chapter.chapterNumber"),
    })
    RepliesCommentResponse toRepliesCommentResponse(Comment comment);
}
