package com.store.book.exception.model;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse<T> {

    private String errorCode;
    private String errorMessage;
    private T message;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
