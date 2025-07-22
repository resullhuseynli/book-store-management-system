package com.store.book.dao.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDtoUpdate {

    @NotBlank(message = "{NotBlank}")
    private String firstName;
    @NotBlank
    private String lastName;
    @Email(message = "{Email}")
    private String email;
}
