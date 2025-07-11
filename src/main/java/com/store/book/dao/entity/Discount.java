package com.store.book.dao.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "discounts")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_id")
    private Long id;

    @Column(name = "discount_percentage")
    private BigDecimal percentage;

    @Column(name = "discount_start_date")
    private LocalDateTime startDate;

    @Column(name = "discount_end_date")
    private LocalDateTime endDate;

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToMany(mappedBy = "discounts")
    private Set<Book> books = new HashSet<>();

}
