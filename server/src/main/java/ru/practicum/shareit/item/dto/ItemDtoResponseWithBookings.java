package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.user.dto.UserDtoResponse;

import java.util.List;

public record ItemDtoResponseWithBookings(
        Long id,
        String name,
        String description,
        boolean available,
        UserDtoResponse userResponseDto,
        ItemDtoRequestId request,
        BookingDtoResponse lastBooking,
        BookingDtoResponse nextBooking,
        List<CommentDtoResponse> comments
) {
}
