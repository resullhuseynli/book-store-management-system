package com.store.book.dao.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoRequest {

    @NotNull
    @NotBlank
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    @NotEmpty
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must have at least 8 characters, including uppercase, lowercase, number, and special character"
    )
    private String password;

    private String firstName;
    private String lastName;

    @Email
    private String email;
}
