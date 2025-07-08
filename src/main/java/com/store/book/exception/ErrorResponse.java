package com.store.book.exception;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse<T> {

    private UUID errorId;
    private T message;
    private final LocalDateTime timestamp = LocalDateTime.now();

    public ErrorResponse(UUID errorId, T message) {
        this.errorId = errorId;
        this.message = message;
    }
}
