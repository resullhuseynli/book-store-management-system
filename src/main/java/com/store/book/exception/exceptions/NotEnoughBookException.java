package com.store.book.exception.exceptions;

public class NotEnoughBookException extends RuntimeException {
    public NotEnoughBookException(String message) {
        super(message);
    }
}
