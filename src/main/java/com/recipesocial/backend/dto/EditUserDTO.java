package com.recipesocial.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditUserDTO {

    @NotBlank(message = "Name cannot be blank", groups = {OnUpdate.class})
    @Size(max = 100, message = "Name can't be longer than 100 characters", groups = {OnUpdate.class})
    private String name;

    @Email(message = "Email must be a valid email", groups = {OnUpdate.class})
    @NotBlank(message = "Email cannot be blank", groups = {OnUpdate.class})
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters", groups = {OnUpdate.class})
    private String password;
}
