package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleUserNotFound(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("Ошибка валидации.");
        problem.setProperty("error", "Ошибка валидации.");
        return problem;
    }

    @ExceptionHandler(ExceptionItemNotFound.class)
    public ProblemDetail handleUserNotFound(ExceptionItemNotFound ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Запрошенная вещь не найдена.");
        return problem;
    }

    @ExceptionHandler(ExceptionUserEmailExists.class)
    public ProblemDetail handleUserNotFound(ExceptionUserEmailExists ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Электронная почта уже занята другим пользователем.");
        return problem;
    }

    @ExceptionHandler(ExceptionUserNotFound.class)
    public ProblemDetail handleUserNotFound(ExceptionUserNotFound ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Запрошенный пользователь не найден.");
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUserNotFound(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problem.setTitle("Внутренняя ошибка сервера.");
        return problem;
    }

}