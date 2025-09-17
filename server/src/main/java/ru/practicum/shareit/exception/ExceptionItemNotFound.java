package ru.practicum.shareit.exception;

public class ExceptionItemNotFound extends RuntimeException {
    public ExceptionItemNotFound(String message) {
        super(message);
    }
}