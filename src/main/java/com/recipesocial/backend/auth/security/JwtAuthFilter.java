package com.recipesocial.backend.auth.security;

import com.recipesocial.backend.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Check if header is present and starts with Bearer
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("No JWT token found in request header");
            filterChain.doFilter(request, response); // skip and continue the chain
            return;
        }

        jwt = authHeader.substring(7); // Strip "Bearer "
        userEmail = jwtService.extractUsername(jwt); // extract email from token
        log.debug("Extracted email from token: {}", userEmail);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            log.debug("Loaded user from DB: {}", userDetails.getUsername());

            if (jwtService.isTokenValid(jwt, userDetails.getUsername())) {
                log.debug("JWT is valid for user: {}", userDetails.getUsername());
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // ✅ Set authentication context
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.debug("Authentication set for user: {}", userDetails.getUsername());
            } else {
                log.warn("Invalid JWT token for user: {}", userDetails.getUsername());
            }
        }

        filterChain.doFilter(request, response);
    }
}
