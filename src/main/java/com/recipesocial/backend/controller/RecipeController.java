package com.recipesocial.backend.controller;

import com.recipesocial.backend.model.Recipe;
import com.recipesocial.backend.model.User;
import com.recipesocial.backend.repository.RecipeRepository;
import com.recipesocial.backend.repository.UserRepository;
import com.recipesocial.backend.auth.security.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/recipes", produces = "application/json")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    // Create a recipe
    @PostMapping(consumes = "application/json")
    public Recipe createRecipe(@RequestBody Recipe recipe) {
        // Get current user
        User currentUser = AuthUtils.getCurrentUser();

        recipe.setAuthor(currentUser);

        return recipeRepository.save(recipe);
    }

    // Get all recipes
    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    // Get a recipe by ID
    @GetMapping("/{id}")
    public Recipe getRecipeById(@PathVariable Long id) {
        return recipeRepository.findById(id).orElse(null);
    }
}
