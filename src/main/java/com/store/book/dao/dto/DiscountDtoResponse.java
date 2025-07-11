package com.store.book.dao.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiscountDtoResponse {

    private Long id;
    private BigDecimal percentage;
    private Set<String> bookNames;
    private boolean isActive;

}
