package com.recipesocial.backend.controller;

import com.recipesocial.backend.dto.CreateRecipeDTO;
import com.recipesocial.backend.dto.EditRecipeDTO;
import com.recipesocial.backend.model.Recipe;
import com.recipesocial.backend.model.Role;
import com.recipesocial.backend.model.User;
import com.recipesocial.backend.repository.RecipeRepository;
import com.recipesocial.backend.auth.security.AuthUtils;
import com.recipesocial.backend.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/recipes", produces = "application/json")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Recipe> createRecipe(@Valid @RequestBody CreateRecipeDTO createRecipeDTO) {
        User currentUser = AuthUtils.getCurrentUser();

        Recipe recipe = new Recipe();
        recipe.setTitle(createRecipeDTO.getTitle());
        recipe.setDescription(createRecipeDTO.getDescription());
        recipe.setIngredients(createRecipeDTO.getIngredients());
        recipe.setInstructions(createRecipeDTO.getInstructions());
        recipe.setAuthor(currentUser);

        Recipe savedRecipe = recipeRepository.save(recipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
    }

    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        return ResponseEntity.ok(recipeRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        return recipeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recipe>> getRecipesByUser(@PathVariable Long userId) {
        // Check if the user exists
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<Recipe> recipes = recipeRepository.findByUserId(userId);

        return ResponseEntity.ok(recipes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editRecipe(@PathVariable Long id, @Valid @RequestBody EditRecipeDTO editRecipeDTO) {
        // Check if the recipe exists
        Recipe recipe = recipeRepository.findById(id).orElse(null);
        if (recipe == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Recipe with id " + id + " not found.");
        }

        // Check if the current user is an admin or the author of the recipe
        User currentUser = AuthUtils.getCurrentUser();
        if (!currentUser.getRole().equals(Role.ADMIN) && !recipe.getAuthor().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only edit your own recipes or if you are an admin.");
        }

        // Update the recipe with non-null fields from the DTO
        if (editRecipeDTO.getTitle() != null) recipe.setTitle(editRecipeDTO.getTitle());
        if (editRecipeDTO.getDescription() != null) recipe.setDescription(editRecipeDTO.getDescription());
        if (editRecipeDTO.getIngredients() != null) recipe.setIngredients(editRecipeDTO.getIngredients());
        if (editRecipeDTO.getInstructions() != null) recipe.setInstructions(editRecipeDTO.getInstructions());

        // Save the updated recipe
        recipeRepository.save(recipe);

        return ResponseEntity.ok("Recipe updated successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable Long id) {
        // Check if the recipe exists
        Recipe recipe = recipeRepository.findById(id).orElse(null);
        if (recipe == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Recipe with id " + id + " not found.");
        }

        // Check if the user is an admin or the author of the recipe
        User currentUser = AuthUtils.getCurrentUser();
        if (!currentUser.getRole().equals(Role.ADMIN) && !recipe.getAuthor().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only delete your own recipes or if you are an admin.");
        }

        // Delete the recipe
        recipeRepository.delete(recipe);

        return ResponseEntity.ok("Recipe deleted successfully.");
    }
}
