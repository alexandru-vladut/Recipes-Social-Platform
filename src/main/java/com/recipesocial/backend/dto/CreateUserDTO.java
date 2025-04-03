package com.recipesocial.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDTO {

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name can't be longer than 100 characters")
    private String name;

    @Email(message = "Email must be a valid email")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
