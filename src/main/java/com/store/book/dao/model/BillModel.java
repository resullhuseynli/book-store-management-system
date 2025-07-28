package com.store.book.dao.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillModel {

    private Long id;
    private String companyName;
    private String companyAddress;
    private List<ProductModel> productList;
    private BigDecimal totalPrice;
    private LocalDateTime createdDate;
}
