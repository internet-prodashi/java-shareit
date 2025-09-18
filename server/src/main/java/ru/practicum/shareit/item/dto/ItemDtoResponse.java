package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.request.dto.ItemRequestDtoRequestCreate;
import ru.practicum.shareit.user.dto.UserDtoResponse;

public record ItemDtoResponse(
        Long id,
        String name,
        String description,
        boolean available,
        UserDtoResponse userDtoResponse,
        ItemRequestDtoRequestCreate request
) {
}
