package com.temirlan.firstspringproject.service;

import com.temirlan.firstspringproject.dto.UserRegistrationDto;
import com.temirlan.firstspringproject.model.User;
import com.temirlan.firstspringproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserRegistrationDto dto) {
        // check if username already exists
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken: " + dto.getUsername());
        }

        // create new user
        User user = new User();
        user.setUsername(dto.getUsername());

        // hash the plain text password before saving
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        // save to database and return
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
}