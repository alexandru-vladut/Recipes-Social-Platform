package com.recipesocial.backend.repository;

import com.recipesocial.backend.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    // Custom JPQL query to find recipes by a specific user (author)
    @Query("SELECT r FROM Recipe r WHERE r.author.id = :userId")
    List<Recipe> findByUserId(Long userId);
}
