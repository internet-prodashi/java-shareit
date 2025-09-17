package ru.practicum.shareit.comment.dto;

import java.time.LocalDateTime;

public record CommentDtoResponse(
        Long id,
        String text,
        String authorName,
        LocalDateTime created
) {
}