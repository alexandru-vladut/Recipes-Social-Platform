package com.recipesocial.backend.controller;

import com.recipesocial.backend.model.Recipe;
import com.recipesocial.backend.repository.RecipeRepository;
import com.recipesocial.backend.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RecipeController(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    // Create a recipe
    @PostMapping
    public Recipe createRecipe(@RequestBody Recipe recipe) {
        // Temporary: find first user and assign them as author
        userRepository.findAll().stream().findFirst().ifPresent(recipe::setAuthor);
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
