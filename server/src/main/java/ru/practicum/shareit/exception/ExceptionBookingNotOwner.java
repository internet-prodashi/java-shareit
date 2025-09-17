package ru.practicum.shareit.exception;

public class ExceptionBookingNotOwner extends RuntimeException {
    public ExceptionBookingNotOwner(String message) {
        super(message);
    }
}
