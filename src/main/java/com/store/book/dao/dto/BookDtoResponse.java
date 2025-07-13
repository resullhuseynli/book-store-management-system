package com.store.book.dao.dto;

import com.store.book.enums.Genre;
import com.store.book.enums.Language;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDtoResponse {

    private Long id;
    private String title;
    private Genre genre;
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private boolean hasDiscount;
    private Integer pageCount;
    private List<Language> languages;
    private String authorName;
    private String publisherName;
}
