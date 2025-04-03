package com.recipesocial.backend.controller;

import com.recipesocial.backend.dto.EditUserDTO;
import com.recipesocial.backend.model.Recipe;
import com.recipesocial.backend.model.Role;
import com.recipesocial.backend.model.User;
import com.recipesocial.backend.repository.UserRepository;
import com.recipesocial.backend.auth.security.AuthUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users", produces = "application/json")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<String> editUser(@PathVariable Long id, @Valid @RequestBody EditUserDTO updateData) {
        // Check if the user exists
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + id + " not found.");
        }

        // Check if the current user is an admin or the user themselves
        User currentUser = AuthUtils.getCurrentUser();
        if (!currentUser.getRole().equals(Role.ADMIN) && !currentUser.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only edit your own details or if you are an admin.");
        }

        // Update the user with non-null fields from the DTO
        if (updateData.getName() != null) user.setName(updateData.getName());
        if (updateData.getEmail() != null) user.setEmail(updateData.getEmail());
        if (updateData.getPassword() != null) user.setPassword(passwordEncoder.encode(updateData.getPassword()));

        // Save the updated user
        userRepository.save(user);

        return ResponseEntity.ok("User updated successfully.");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        // Check if the user exists
        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + id + " not found.");
        }

        // Check if the current user is the user themselves
        User currentUser = AuthUtils.getCurrentUser();
        if (currentUser.getId().equals(id)) {
            return ResponseEntity.badRequest().body("You cannot delete your own account.");
        }

        // Delete the user
        userRepository.deleteById(id);

        return ResponseEntity.ok("User deleted successfully.");
    }
}
