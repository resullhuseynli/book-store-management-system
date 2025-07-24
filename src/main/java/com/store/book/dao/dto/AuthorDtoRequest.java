package com.store.book.dao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDtoRequest {

    @NotBlank(message = "NotBlank")
    private String name;

    @Size(max = 50, message = "Size")
    private String aboutUrl;
}
