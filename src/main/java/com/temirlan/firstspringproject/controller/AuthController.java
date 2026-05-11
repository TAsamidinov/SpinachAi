package com.temirlan.firstspringproject.controller;

import com.temirlan.firstspringproject.dto.LoginDto;
import com.temirlan.firstspringproject.dto.UserRegistrationDto;
import com.temirlan.firstspringproject.model.User;
import com.temirlan.firstspringproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegistrationDto dto) {
        try {
            userService.registerUser(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User registered successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto dto) {
        try {
            User user = userService.findByUsername(dto.getUsername());
            boolean passwordMatches = passwordEncoder.matches(
                dto.getPassword(),
                user.getPasswordHash()
            );
            if (passwordMatches) {
                return ResponseEntity.ok("Login successful!");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid password!");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}