package com.store.book.dao.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDtoRequest {

    private Long bookId;
    private int quantity;
}
