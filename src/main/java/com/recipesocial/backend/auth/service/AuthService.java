package com.recipesocial.backend.auth.service;

import com.recipesocial.backend.auth.dto.AuthResponseDTO;
import com.recipesocial.backend.auth.dto.AuthenticationRequestDTO;
import com.recipesocial.backend.dto.CreateUserDTO;
import com.recipesocial.backend.exception.EmailAlreadyExistsException;
import com.recipesocial.backend.exception.InvalidCredentialsException;
import com.recipesocial.backend.exception.UserNotFoundException;
import com.recipesocial.backend.model.Role;
import com.recipesocial.backend.model.User;
import com.recipesocial.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO register(CreateUserDTO request, Role role) {
        // ✅ Check if user already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email is already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        userRepository.save(user);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());

        String token = jwtService.generateToken(claims, user.getEmail());
        return new AuthResponseDTO(token);
    }

    public AuthResponseDTO authenticate(AuthenticationRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());

        String token = jwtService.generateToken(claims, user.getEmail());
        return new AuthResponseDTO(token);
    }
}
