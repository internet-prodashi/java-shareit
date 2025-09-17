package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record ItemRequestDtoRequestCreate(
        @NotBlank(message = "Описание вещи не может быть пустым")
        String description,
        LocalDateTime created
) {
}