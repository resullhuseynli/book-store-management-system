package com.store.book.exception;

import com.store.book.exception.constants.ErrorCode;
import com.store.book.exception.exceptions.*;
import com.store.book.exception.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
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

import static com.store.book.exception.constants.ErrorCode.*;
import static com.store.book.exception.constants.ErrorMessage.*;
import static org.springframework.http.ResponseEntity.badRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(EntityContainException.class)
    public ResponseEntity<ErrorResponse<String>> handleEntityContainException(EntityContainException entityContainException) {
        log.error(ErrorCode.ENTITY_CONTAINS_ERROR_CODE + ": {}", entityContainException.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse.<String>builder()
                .errorCode(ENTITY_CONTAINS_ERROR_CODE)
                .errorMessage(getLocalizedMessage(ENTITY_CONTAINS_ERROR_MESSAGE))
                .message(entityContainException.getMessage())
                .build());
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    public ResponseEntity<ErrorResponse<String>> handleNotEnoughMoneyException(NotEnoughMoneyException notEnoughMoneyException) {
        log.error(ErrorCode.NOT_ENOUGH_MONEY_ERROR_CODE + ": {}", notEnoughMoneyException.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse.<String>builder()
                .errorCode(NOT_ENOUGH_MONEY_ERROR_CODE)
                .errorMessage(getLocalizedMessage(NOT_ENOUGH_MONEY_ERROR_MESSAGE))
                .message(notEnoughMoneyException.getMessage())
                .build());
    }

    @ExceptionHandler(NotEnoughBookException.class)
    public ResponseEntity<ErrorResponse<String>> handleNotEnoughItemException(NotEnoughBookException notEnoughBookException) {
        log.error(ErrorCode.NOT_ENOUGH_BOOK_ERROR_CODE + ": {}", notEnoughBookException.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse.<String>builder()
                .errorCode(NOT_ENOUGH_BOOK_ERROR_CODE)
                .errorMessage(getLocalizedMessage(NOT_ENOUGH_BOOK_ERROR_MESSAGE))
                .message(notEnoughBookException.getMessage())
                .build());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse<String>> handleNotFoundException(NotFoundException notFoundException) {
        log.error(ErrorCode.NOT_FOUND_ERROR_CODE + ": {}", notFoundException.getMessage());
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
        log.error(ErrorCode.METHOD_ARGUMENT_NOT_VALID_ERROR_CODE + ": {}", methodArgumentNotValidException.getMessage());
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
        log.error(ErrorCode.USERNAME_NOT_FOUND_ERROR_CODE + ": {}", usernameNotFoundException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.<String>builder()
                .errorCode(USERNAME_NOT_FOUND_ERROR_CODE)
                .errorMessage(getLocalizedMessage(USERNAME_NOT_FOUND_ERROR_MESSAGE))
                .message(usernameNotFoundException.getMessage())
                .build());
    }

    @ExceptionHandler(ImageIsNotAvailableException.class)
    public ResponseEntity<ErrorResponse<String>> handleImageIsNotAvailableException(ImageIsNotAvailableException imageIsNotAvailableException) {
        log.error(ErrorCode.IMAGE_IS_NOT_AVAILABLE_ERROR_CODE + ": {}", imageIsNotAvailableException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.<String>builder()
                .errorCode(IMAGE_IS_NOT_AVAILABLE_ERROR_CODE)
                .errorMessage(getLocalizedMessage(IMAGE_IS_NOT_AVAILABLE_ERROR_MESSAGE))
                .message(imageIsNotAvailableException.getMessage())
                .build());
    }

    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ErrorResponse<String>> handleImageIsNotAvailableException(TimeoutException timeoutException) {
        log.error(ErrorCode.TIMEOUT_EXCEPTION_ERROR_CODE + ": {}", timeoutException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.<String>builder()
                .errorCode(TIMEOUT_EXCEPTION_ERROR_CODE)
                .errorMessage(getLocalizedMessage(TIMEOUT_EXCEPTION_ERROR_MESSAGE))
                .message(timeoutException.getMessage())
                .build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse<String>> handleBadCredentialsException(BadCredentialsException badCredentialsException) {
        log.error(ErrorCode.BAD_CREDENTIALS_ERROR_CODE + ": {}", badCredentialsException.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(badRequest().body(ErrorResponse.<String>builder()
                .errorCode(BAD_CREDENTIALS_ERROR_CODE)
                .errorMessage(getLocalizedMessage(BAD_CREDENTIALS_ERROR_MESSAGE))
                .message(badCredentialsException.getMessage())
                .build()).getBody());
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorResponse<String>> handleUserAlreadyExistException(UserAlreadyExistException userAlreadyExistException) {
        log.error(ErrorCode.USER_ALREADY_EXISTS_ERROR_CODE + ": {}", userAlreadyExistException.getMessage());
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
