package ru.practicum.shareit.exception;

public class ExceptionBookingNotFound extends RuntimeException {
    public ExceptionBookingNotFound(String message) {
        super(message);
    }
}
