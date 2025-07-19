package com.store.book.dao.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDtoResponse {

    private Long id;
    private String bookName;
    private BigDecimal price;
    private int quantity;
    private BigDecimal totalPrice;

}
