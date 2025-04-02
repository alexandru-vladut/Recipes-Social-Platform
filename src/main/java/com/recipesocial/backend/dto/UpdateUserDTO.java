package com.recipesocial.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {
    private String name;
    private String email;
    private String password; // optional, will only be updated if provided
    private String role;
}
