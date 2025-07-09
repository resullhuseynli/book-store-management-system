package com.store.book.dao.dto;

import com.store.book.enums.Genre;
import com.store.book.enums.Language;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDtoRequest {

    private String title;
    private Genre genre;

    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;
    private Integer amount;
    private Integer pageCount;
    private Set<Language> languages;
    private Long authorId;
    private Long publisherId;
}
