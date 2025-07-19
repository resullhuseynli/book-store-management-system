package com.store.book.dao.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDtoResponse {

    private Long id;
    private BigDecimal totalPrice;
    List<ItemDtoResponse> items;
}
