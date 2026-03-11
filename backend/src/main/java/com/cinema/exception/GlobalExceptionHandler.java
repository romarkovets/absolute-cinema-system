package com.cinema.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String TIMESTAMP = "timestamp";
    private static final String STATUS = "status";
    private static final String ERROR = "error";
    private static final String MESSAGE = "message";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(TIMESTAMP, LocalDateTime.now());
        response.put(STATUS, HttpStatus.NOT_FOUND.value());
        response.put(ERROR, "Not Found");
        response.put(MESSAGE, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler({SeatAlreadyTakenException.class, DuplicateSessionException.class, DuplicateResourceException.class})
    public ResponseEntity<Map<String, Object>> handleBadRequest(RuntimeException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(TIMESTAMP, LocalDateTime.now());
        response.put(STATUS, HttpStatus.BAD_REQUEST.value());
        response.put(ERROR, "Bad Request");
        response.put(MESSAGE, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(TIMESTAMP, LocalDateTime.now());
        response.put(STATUS, HttpStatus.BAD_REQUEST.value());
        response.put(ERROR, "Validation Error");
        response.put(MESSAGE, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}