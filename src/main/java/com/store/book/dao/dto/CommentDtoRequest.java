package com.store.book.dao.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoRequest {

    private Long bookId;
    private String description;
    @Max(value = 5, message = "BetweenOneToFive")
    @Min(value = 1, message = "BetweenOneToFive")
    private Integer rating;

}
