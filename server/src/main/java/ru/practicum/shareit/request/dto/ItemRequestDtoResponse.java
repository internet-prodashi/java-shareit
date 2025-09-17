package ru.practicum.shareit.request.dto;

import java.time.LocalDateTime;

public record ItemRequestDtoResponse(
        Long id,
        String description,
        LocalDateTime created
) {
}