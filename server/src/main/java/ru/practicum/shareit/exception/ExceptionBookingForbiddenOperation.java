package ru.practicum.shareit.exception;

public class ExceptionBookingForbiddenOperation extends RuntimeException {
    public ExceptionBookingForbiddenOperation(String message) {
        super(message);
    }
}
