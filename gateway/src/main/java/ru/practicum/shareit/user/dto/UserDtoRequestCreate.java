package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDtoRequestCreate(
        @NotBlank(message = "Имя пользователя не может быть пустым")
        String name,
        @NotBlank(message = "Электронная почта пользователя не может быть пустая")
        @Email
        String email
) {
}