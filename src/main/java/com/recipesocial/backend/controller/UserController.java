package com.recipesocial.backend.controller;

import com.recipesocial.backend.model.User;
import com.recipesocial.backend.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/test")
    public List<User> createAndReturnTestUser() {
        // Save a dummy user
        User user = new User(null, "John Doe", "john@example.com", "123456", "USER");
        userRepository.save(user);

        // Return all users in DB
        return userRepository.findAll();
    }
}
