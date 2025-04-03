package com.recipesocial.backend.auth.security;

import com.recipesocial.backend.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found.");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof AppUserDetails userDetails) {
            return userDetails.getUser();
        }

        throw new IllegalStateException("Unexpected authentication principal type.");
    }
}
