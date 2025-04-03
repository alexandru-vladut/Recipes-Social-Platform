package com.recipesocial.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditRecipeDTO {

    @NotBlank(message = "Title cannot be blank", groups = {OnUpdate.class})
    @Size(max = 100, message = "Title can't be longer than 100 characters", groups = {OnUpdate.class})
    private String title;

    @NotBlank(message = "Description cannot be blank", groups = {OnUpdate.class})
    @Size(max = 1000, message = "Description can't be longer than 1000 characters", groups = {OnUpdate.class})
    private String description;

    @Size(max = 1000, message = "Ingredients can't be longer than 1000 characters", groups = {OnUpdate.class})
    private String ingredients;

    @Size(max = 2000, message = "Instructions can't be longer than 2000 characters", groups = {OnUpdate.class})
    private String instructions;
}
