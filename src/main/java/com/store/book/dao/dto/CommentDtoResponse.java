package com.store.book.dao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoResponse {

    private Long id;
    private String userName;
    private String description;
    private Integer rating;
    private Long bookId;
    private String bookTitle;
}
