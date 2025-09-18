package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record BookingDtoRequestCreate(
        @NotNull(message = "Необходимо указать идентификатор вещи")
        @Positive(message = "Идентификатор вещи должен быть больше нуля")
        Long itemId,
        @NotNull(message = "Необходимо указать дату и время начала аренды")
        @FutureOrPresent(message = "Нельзя указывать прошедшую дату и время начала аренды")
        LocalDateTime start,
        @FutureOrPresent(message = "Нельзя указывать прошедшую дату и время окончания аренды")
        @NotNull(message = "Необходимо указать дату и время окончания аренды")
        LocalDateTime end
) {
}
