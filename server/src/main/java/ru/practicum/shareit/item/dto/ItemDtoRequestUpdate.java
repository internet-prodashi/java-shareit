package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.request.dto.ItemRequestDtoRequestCreate;

public record ItemDtoRequestUpdate(
        String name,
        String description,
        Boolean available,
        ItemRequestDtoRequestCreate request
) {
}