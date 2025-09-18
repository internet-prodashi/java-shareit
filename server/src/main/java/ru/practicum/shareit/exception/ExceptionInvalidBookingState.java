package ru.practicum.shareit.exception;

public class ExceptionInvalidBookingState extends RuntimeException {
    public ExceptionInvalidBookingState(String message) {
        super(message);
    }
}
