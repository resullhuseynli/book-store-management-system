package com.store.book.dao.dto;

import com.store.book.enums.Genre;
import com.store.book.enums.Language;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDtoRequest {

    private String title;
    private Genre genre;

    @NotNull(message = "NotNull")
    @DecimalMin(value = "0.01", message = "CannotBeNegative")
    private BigDecimal price;

    @Min(value = 0, message = "CannotBeNegative")
    private Integer amount;

    @Min(value = 0, message = "CannotBeNegative")
    private Integer pageCount;
    private String description;
    private List<Language> languages;
    @Min(value = 0, message = "CannotBeNegative")
    private Long authorId;
    @Min(value = 0, message = "CannotBeNegative")
    private Long publisherId;
}
