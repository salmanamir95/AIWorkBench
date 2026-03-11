package com.example.Auth.api.exception;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.Auth.api.response.GenericResponse;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GenericResponse<Void>> handleIllegalArgument(final IllegalArgumentException ex) {
        return ResponseEntity.ok(GenericResponse.failure(ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<GenericResponse<Void>> handleIllegalState(final IllegalStateException ex) {
        return ResponseEntity.ok(GenericResponse.failure(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponse<Void>> handleValidation(final MethodArgumentNotValidException ex) {
        final String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining("; "));

        return ResponseEntity.ok(GenericResponse.failure(message.isBlank() ? "Validation failed" : message));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<GenericResponse<Void>> handleConstraintViolation(final ConstraintViolationException ex) {
        return ResponseEntity.ok(GenericResponse.failure(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<Void>> handleGeneric(final Exception ex) {
        return ResponseEntity.ok(GenericResponse.failure("Internal server error"));
    }

    private String formatFieldError(final FieldError error) {
        final String field = error.getField();
        final String defaultMessage = error.getDefaultMessage() == null ? "invalid" : error.getDefaultMessage();
        return field + ": " + defaultMessage;
    }
}
