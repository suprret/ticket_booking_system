package com.ticketbooking.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ErrorResponse(String timestamp, int status, String error, String message) {}

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(ResourceNotFoundException ex) {
        return new ErrorResponse(
                LocalDateTime.now().toString(),
                404,
                "Not Found",
                ex.getMessage()
        );
    }

    @ExceptionHandler(SeatAlreadyBookedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflict(SeatAlreadyBookedException ex) {
        return new ErrorResponse(
                LocalDateTime.now().toString(),
                409,
                "Conflict",
                ex.getMessage()
        );
    }
}