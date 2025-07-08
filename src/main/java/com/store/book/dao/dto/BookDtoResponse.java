package com.store.book.dao.dto;

import com.store.book.enums.Genre;
import com.store.book.enums.Language;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDtoResponse {

    private Long id;
    private String title;
//    private MultipartFile coverImage;
    private Genre genre;
    private BigDecimal price;
    private Integer pageCount;
    private Set<Language> languages;
    private String authorName;
    private String publisherName;
}
