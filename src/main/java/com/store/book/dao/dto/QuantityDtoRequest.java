package com.store.book.dao.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuantityDtoRequest {

    private Long bookId;
    @Min(value = 1, message = "CannotBeNegative")
    private int quantity;
}
