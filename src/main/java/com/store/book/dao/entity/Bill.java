package com.store.book.dao.entity;

import com.store.book.dao.model.ProductModel;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "bills")
public class Bill {

    @Id
    private Long id;
    private String companyName;
    private String companyAddress;
    private List<ProductModel> productList;
    private BigDecimal totalPrice;
    private LocalDateTime createdDate;
}
