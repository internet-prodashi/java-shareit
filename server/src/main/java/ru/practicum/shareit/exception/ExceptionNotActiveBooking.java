package ru.practicum.shareit.exception;

public class ExceptionNotActiveBooking extends RuntimeException {
    public ExceptionNotActiveBooking(String message) {
        super(message);
    }
}
