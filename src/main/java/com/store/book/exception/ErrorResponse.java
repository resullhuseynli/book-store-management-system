package com.store.book.exception;


import lombok.*;

import java.rmi.server.UID;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse<T> {

    private UID errorId;
    private T message;
    private final LocalDateTime timestamp =  LocalDateTime.now();

    public ErrorResponse(UID errorId, T message) {
        this.errorId = errorId;
        this.message = message;
    }
}
