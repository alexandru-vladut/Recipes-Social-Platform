package com.recipesocial.backend.auth;

import com.recipesocial.backend.auth.dto.AuthResponseDTO;
import com.recipesocial.backend.auth.dto.AuthenticationRequestDTO;
import com.recipesocial.backend.auth.dto.RegisterRequestDTO;
import com.recipesocial.backend.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value ="/auth", produces = "application/json")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthenticationRequestDTO request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
