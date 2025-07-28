package com.store.book.dao.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductModel {

    private String name;
    private Integer amount;
    private BigDecimal price;
}
