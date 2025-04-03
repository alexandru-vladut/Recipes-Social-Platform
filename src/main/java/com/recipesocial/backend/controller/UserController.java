package com.recipesocial.backend.controller;

import com.recipesocial.backend.dto.UpdateUserDTO;
import com.recipesocial.backend.model.User;
import com.recipesocial.backend.repository.UserRepository;
import com.recipesocial.backend.auth.security.AuthUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserDTO updateData) {
        return userRepository.findById(id).map(user -> {
            if (updateData.getName() != null) user.setName(updateData.getName());
            if (updateData.getEmail() != null) user.setEmail(updateData.getEmail());
            if (updateData.getPassword() != null && !updateData.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(updateData.getPassword()));
            }
            if (updateData.getRole() != null) user.setRole(updateData.getRole());

            userRepository.save(user);
            return ResponseEntity.ok("User updated successfully.");
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        // Get current user
        User currentUser = AuthUtils.getCurrentUser();

        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        if (currentUser.getId().equals(id)) {
            return ResponseEntity.badRequest().body("You cannot delete your own account.");
        }

        userRepository.deleteById(id);
        return ResponseEntity.ok("User deleted successfully.");
    }
}
