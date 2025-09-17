package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ItemDtoRequestCreate(
        @NotBlank(message = "Название вещи не может быть пустым")
        String name,
        @NotBlank(message = "Описание вещи не может быть пустым")
        String description,
        @NotNull(message = "Необходимо указать статус доступа к аренде")
        Boolean available,
        Long requestId
) {
}
