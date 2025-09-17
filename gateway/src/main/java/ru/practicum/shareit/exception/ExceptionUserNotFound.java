package ru.practicum.shareit.exception;

public class ExceptionUserNotFound extends RuntimeException {
    public ExceptionUserNotFound(String message) {
        super(message);
    }
}
