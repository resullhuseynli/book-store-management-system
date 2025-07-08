package com.store.book.dao.dto;

import com.store.book.enums.Genre;
import com.store.book.enums.Language;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDtoRequest {

    private String title;
    private Genre genre;
    private BigDecimal price;
    private Integer amount;
    private Integer pageCount;
    private Set<Language> languages;
    private Long authorId;
    private Long publisherId;
}
