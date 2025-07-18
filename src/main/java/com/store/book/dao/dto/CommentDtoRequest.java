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
    @Max(value = 5, message = "Rating have to be between 1-5")
    @Min(value = 1, message = "Rating have to be between 1-5")
    private Integer rating;

}
