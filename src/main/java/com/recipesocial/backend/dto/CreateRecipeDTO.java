package com.recipesocial.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRecipeDTO {

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100, message = "Title can't be longer than 100 characters")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 1000, message = "Description can't be longer than 1000 characters")
    private String description;

    @Size(max = 1000, message = "Ingredients can't be longer than 1000 characters")
    private String ingredients;

    @Size(max = 2000, message = "Instructions can't be longer than 2000 characters")
    private String instructions;
}
