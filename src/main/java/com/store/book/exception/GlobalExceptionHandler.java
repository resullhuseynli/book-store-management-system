package com.store.book.exception;

import com.store.book.exception.exception_model.ErrorCode;
import com.store.book.exception.exception_model.ErrorMessage;
import com.store.book.exception.exceptions.*;
import com.store.book.exception.model.ErrorResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

import static com.store.book.exception.exception_model.ErrorCode.*;
import static com.store.book.exception.exception_model.ErrorCode.TIMEOUT_EXCEPTION_ERROR_CODE;
import static com.store.book.exception.exception_model.ErrorMessage.*;
import static org.springframework.http.ResponseEntity.badRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(EntityContainException.class)
    public ResponseEntity<ErrorResponse<String>> handleEntityContainException(EntityContainException entityContainException) {
        return ResponseEntity.badRequest().body(ErrorResponse.<String>builder()
                .errorCode(ENTITY_CONTAINS_ERROR_CODE)
                .errorMessage(getLocalizedMessage(ENTITY_CONTAINS_ERROR_MESSAGE))
                .message(entityContainException.getMessage())
                .build());
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    public ResponseEntity<ErrorResponse<String>> handleNotEnoughMoneyException(NotEnoughMoneyException notEnoughMoneyException) {
        return ResponseEntity.badRequest().body(ErrorResponse.<String>builder()
                .errorCode(NOT_ENOUGH_MONEY_ERROR_CODE)
                .errorMessage(getLocalizedMessage(NOT_ENOUGH_MONEY_ERROR_MESSAGE))
                .message(notEnoughMoneyException.getMessage())
                .build());
    }

    @ExceptionHandler(NotEnoughBookException.class)
    public ResponseEntity<ErrorResponse<String>> handleNotEnoughItemException(NotEnoughBookException notEnoughBookException) {
        return ResponseEntity.badRequest().body(ErrorResponse.<String>builder()
                .errorCode(NOT_ENOUGH_BOOK_ERROR_CODE)
                .errorMessage(getLocalizedMessage(NOT_ENOUGH_BOOK_ERROR_MESSAGE))
                .message(notEnoughBookException.getMessage())
                .build());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse<String>> handleNotFoundException(NotFoundException notFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.<String>builder()
                .errorCode(NOT_FOUND_ERROR_CODE)
                .errorMessage(getLocalizedMessage(NOT_FOUND_ERROR_MESSAGE))
                .message(notFoundException.getMessage())
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<Map<String, List<String>>>> handleValidationErrors(MethodArgumentNotValidException methodArgumentNotValidException) {
        List<String> validations = methodArgumentNotValidException.getBindingResult()
                .getFieldErrors()
                .stream()
                .filter(Objects::nonNull)
                .map(error -> {
                    assert error.getDefaultMessage() != null;
                    return getLocalizedMessage(error.getDefaultMessage());
                })
                .toList();
        return ResponseEntity.badRequest().body(ErrorResponse.<Map<String, List<String>>>builder()
                .errorCode(METHOD_ARGUMENT_NOT_VALID_ERROR_CODE)
                .errorMessage(getLocalizedMessage(METHOD_ARGUMENT_NOT_VALID_ERROR_MESSAGE))
                .message(getErrorsMap(validations))
                .build());
    }

    // TODO MAKE IOEXCEPTION CODE
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse<String>> handleIOException(IOException ioException) {
        return ResponseEntity.badRequest().body(ErrorResponse.<String>builder()
                .errorCode(METHOD_ARGUMENT_NOT_VALID_ERROR_CODE)
                .errorMessage(getLocalizedMessage(METHOD_ARGUMENT_NOT_VALID_ERROR_MESSAGE))
                .message(ioException.getMessage())
                .build());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse<String>> handleUsernameNotFoundException(UsernameNotFoundException usernameNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.<String>builder()
                .errorCode(USERNAME_NOT_FOUND_ERROR_CODE)
                .errorMessage(getLocalizedMessage(USERNAME_NOT_FOUND_ERROR_MESSAGE))
                .message(usernameNotFoundException.getMessage())
                .build());
    }

    @ExceptionHandler(ImageIsNotAvailableException.class)
    public ResponseEntity<ErrorResponse<String>> handleImageIsNotAvailableException(ImageIsNotAvailableException imageIsNotAvailableException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.<String>builder()
                .errorCode(IMAGE_IS_NOT_AVAILABLE_ERROR_CODE)
                .errorMessage(getLocalizedMessage(IMAGE_IS_NOT_AVAILABLE_ERROR_MESSAGE))
                .message(imageIsNotAvailableException.getMessage())
                .build());
    }

    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ErrorResponse<String>> handleImageIsNotAvailableException(TimeoutException timeoutException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.<String>builder()
                .errorCode(TIMEOUT_EXCEPTION_ERROR_CODE)
                .errorMessage(getLocalizedMessage(TIMEOUT_EXCEPTION_ERROR_MESSAGE))
                .message(timeoutException.getMessage())
                .build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse<String>> handleBadCredentialsException(BadCredentialsException badCredentialsException) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(badRequest().body(ErrorResponse.<String>builder()
                .errorCode(BAD_CREDENTIALS_ERROR_CODE)
                .errorMessage(getLocalizedMessage(BAD_CREDENTIALS_ERROR_MESSAGE))
                .message(badCredentialsException.getMessage())
                .build()).getBody());
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorResponse<String>> handleUserAlreadyExistException(UserAlreadyExistException userAlreadyExistException) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(badRequest().body(ErrorResponse.<String>builder()
                .errorCode(USER_ALREADY_EXISTS_ERROR_CODE)
                .errorMessage(getLocalizedMessage(USER_ALREADY_EXISTS_ERROR_MESSAGE))
                .message(userAlreadyExistException.getMessage())
                .build()).getBody());
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("Validations", errors);
        return errorResponse;
    }

    private String getLocalizedMessage(String message) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(message, null, locale);
    }
}
