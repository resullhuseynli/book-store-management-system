package com.store.book.exception;

import com.store.book.exception.exceptions.EntityContainException;
import com.store.book.exception.exceptions.NotFoundException;
import com.store.book.exception.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.badRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityContainException.class)
    public ResponseEntity<ErrorResponse<String>> handleEntityContainException(EntityContainException entityContainException) {
        return badRequest().body(new ErrorResponse<>(UUID.randomUUID(), entityContainException.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse<String>> handleNotFoundException(NotFoundException notFoundException) {
        return badRequest().body(new ErrorResponse<>(UUID.randomUUID(), notFoundException.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<Map<String, List<String>>>> handleValidationErrors(MethodArgumentNotValidException methodArgumentNotValidException) {
        List<String> errors = methodArgumentNotValidException.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList();
        return new ResponseEntity<>(new ErrorResponse<>(UUID.randomUUID(), getErrorsMap(errors)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse<String>> handleIOException(IOException ioException) {
        return badRequest().body(new ErrorResponse<>(UUID.randomUUID(), ioException.getMessage()));
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
}
