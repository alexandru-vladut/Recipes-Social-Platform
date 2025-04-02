package com.recipesocial.backend.controller;

import com.recipesocial.backend.model.Recipe;
import com.recipesocial.backend.model.User;
import com.recipesocial.backend.repository.RecipeRepository;
import com.recipesocial.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    // Create a recipe
    @PostMapping
    public Recipe createRecipe(@RequestBody Recipe recipe, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();  // This works because User implements UserDetails
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
