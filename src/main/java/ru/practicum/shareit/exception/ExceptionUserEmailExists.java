package ru.practicum.shareit.exception;

public class ExceptionUserEmailExists extends RuntimeException {
    public ExceptionUserEmailExists(String message) {
        super(message);
    }
}
