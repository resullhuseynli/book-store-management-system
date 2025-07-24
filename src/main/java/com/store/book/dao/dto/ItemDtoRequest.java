package com.store.book.dao.dto;

import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDtoRequest {

    private Long bookId;
    @Min(value = 1, message = "CannotBeNegative")
    private int quantity;
}
