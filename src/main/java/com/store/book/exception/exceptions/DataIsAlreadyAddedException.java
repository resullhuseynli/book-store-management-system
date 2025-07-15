package com.store.book.exception.exceptions;

public class DataIsAlreadyAddedException extends RuntimeException {
    public DataIsAlreadyAddedException(String message) {
        super(message);
    }
}
