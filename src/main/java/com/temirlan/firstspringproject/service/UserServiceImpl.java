package com.temirlan.firstspringproject.service;

import com.temirlan.firstspringproject.dto.UserRegistrationDto;
import com.temirlan.firstspringproject.exception.UserNotFoundException;
import com.temirlan.firstspringproject.exception.UsernameAlreadyTakenException;
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
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new UsernameAlreadyTakenException("Username already taken: " + dto.getUsername());
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
    }
}