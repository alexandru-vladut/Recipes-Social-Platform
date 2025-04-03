package com.recipesocial.backend.auth.dto;

import com.recipesocial.backend.model.Role;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {
    @NotBlank
    private String name;
    @Email
    @NotBlank
    private String email;
    @Size(min = 6)
    private String password;
    private Role role = Role.USER;
}
