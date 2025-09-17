package ru.practicum.shareit.exception;

public class ExceptionBookingNotAvailable extends RuntimeException {
    public ExceptionBookingNotAvailable(String message) {
        super(message);
    }
}
