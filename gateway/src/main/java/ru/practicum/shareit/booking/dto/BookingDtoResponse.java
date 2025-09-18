package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.user.dto.UserDtoResponse;

import java.time.LocalDateTime;

public record BookingDtoResponse(
        Long id,
        LocalDateTime start,
        LocalDateTime end,
        Status status,
        ItemDtoResponse item,
        UserDtoResponse booker
) {
}