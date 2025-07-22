package com.store.book.dao.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthDtoRequest {

    @NotBlank(message = "NotBlank")
    private String username;
    private String password;
}
