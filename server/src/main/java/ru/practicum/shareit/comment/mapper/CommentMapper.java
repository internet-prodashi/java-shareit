package ru.practicum.shareit.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "item", source = "item")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "text", source = "text")
    Comment toCommentDtoRequestCreate(String text, Item item, User author);

    @Mapping(target = "authorName", source = "author.name")
    CommentDtoResponse toCommentDtoResponse(Comment comment);

    List<CommentDtoResponse> toListCommentDtoResponse(List<Comment> comments);
}