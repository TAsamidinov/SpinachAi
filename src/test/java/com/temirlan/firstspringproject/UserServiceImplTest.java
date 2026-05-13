package com.temirlan.firstspringproject;

import com.temirlan.firstspringproject.dto.UserRegistrationDto;
import com.temirlan.firstspringproject.model.User;
import com.temirlan.firstspringproject.repository.UserRepository;
import com.temirlan.firstspringproject.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    // create a fake UserRepository — no real database involved
    @Mock
    private UserRepository userRepository;

    // create a fake PasswordEncoder — no real hashing involved
    @Mock
    private PasswordEncoder passwordEncoder;

    // inject both fakes into UserServiceImpl automatically
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void registerUser_Success() {
        // ARRANGE — set up the scenario
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("temirlan");
        dto.setPassword("hello123");

        // fake: username doesn't exist yet
        when(userRepository.findByUsername("temirlan"))
                .thenReturn(Optional.empty());

        // fake: encoder returns a hashed version
        when(passwordEncoder.encode("hello123"))
                .thenReturn("hashedPassword");

        // fake: repository saves and returns the user
        User savedUser = new User();
        savedUser.setUsername("temirlan");
        savedUser.setPasswordHash("hashedPassword");
        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        // ACT — run the method
        User result = userService.registerUser(dto);

        // ASSERT — check the result
        assertEquals("temirlan", result.getUsername());
        assertEquals("hashedPassword", result.getPasswordHash());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_UsernameAlreadyTaken() {
        // ARRANGE — username already exists
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("temirlan");
        dto.setPassword("hello123");

        User existingUser = new User();
        existingUser.setUsername("temirlan");

        when(userRepository.findByUsername("temirlan"))
                .thenReturn(Optional.of(existingUser));

        // ACT & ASSERT — expect an exception
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.registerUser(dto));

        assertEquals("Username already taken: temirlan", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void findByUsername_Success() {
        // ARRANGE
        User user = new User();
        user.setUsername("temirlan");

        when(userRepository.findByUsername("temirlan"))
                .thenReturn(Optional.of(user));

        // ACT
        User result = userService.findByUsername("temirlan");

        // ASSERT
        assertEquals("temirlan", result.getUsername());
    }

    @Test
    void findByUsername_NotFound() {
        // ARRANGE — user doesn't exist
        when(userRepository.findByUsername("nobody"))
                .thenReturn(Optional.empty());

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.findByUsername("nobody"));

        assertEquals("User not found: nobody", exception.getMessage());
    }
}