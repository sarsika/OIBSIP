package com.library.controller;

import com.library.model.User;
import com.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public User register(@RequestBody User newUser) {
        // every new signup is a normal "USER", not admin
        newUser.setRole("USER");
        return userRepository.save(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        User found = userRepository.findByEmail(loginRequest.getEmail());

        if (found == null || !found.getPassword().equals(loginRequest.getPassword())) {
            // send back a proper JSON error so the frontend can read it
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid email or password");
            return ResponseEntity.status(401).body(error);
        }

        return ResponseEntity.ok(found);
    }
}