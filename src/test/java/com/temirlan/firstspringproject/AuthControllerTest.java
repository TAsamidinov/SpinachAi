package com.temirlan.firstspringproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.temirlan.firstspringproject.controller.AuthController;
import com.temirlan.firstspringproject.dto.LoginDto;
import com.temirlan.firstspringproject.dto.UserRegistrationDto;
import com.temirlan.firstspringproject.exception.UserNotFoundException;
import com.temirlan.firstspringproject.exception.UsernameAlreadyTakenException;
import com.temirlan.firstspringproject.model.User;
import com.temirlan.firstspringproject.security.JwtUtil;
import com.temirlan.firstspringproject.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.temirlan.firstspringproject.config.SecurityConfig;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil; 

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void register_Success() throws Exception {
        // ARRANGE
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("temirlan");
        dto.setPassword("hello123");

        User savedUser = new User();
        savedUser.setUsername("temirlan");

        when(userService.registerUser(any(UserRegistrationDto.class)))
                .thenReturn(savedUser);

        // ACT & ASSERT
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully!"));
    }

    @Test
        void register_UsernameAlreadyTaken() throws Exception {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("temirlan");
        dto.setPassword("hello123");

       when(userService.registerUser(any(UserRegistrationDto.class)))
    .thenThrow(new UsernameAlreadyTakenException("Username already taken: temirlan"));

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                // now expects JSON not plain text
                .andExpect(jsonPath("$.error").value("Username already taken: temirlan"));
        }

    @Test
void login_Success() throws Exception {
    LoginDto dto = new LoginDto();
    dto.setUsername("temirlan");
    dto.setPassword("hello123");

    User user = new User();
    user.setUsername("temirlan");
    user.setPasswordHash("hashedPassword");

    when(userService.findByUsername("temirlan")).thenReturn(user);
    when(passwordEncoder.matches("hello123", "hashedPassword")).thenReturn(true);
    when(jwtUtil.generateToken("temirlan")).thenReturn("fake-jwt-token");

    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("fake-jwt-token"))
            .andExpect(jsonPath("$.username").value("temirlan"));
}

    @Test
    void login_WrongPassword() throws Exception {
        // ARRANGE
        LoginDto dto = new LoginDto();
        dto.setUsername("temirlan");
        dto.setPassword("wrongpassword");

        User user = new User();
        user.setUsername("temirlan");
        user.setPasswordHash("hashedPassword");

        when(userService.findByUsername("temirlan")).thenReturn(user);
        when(passwordEncoder.matches("wrongpassword", "hashedPassword")).thenReturn(false);

        // ACT & ASSERT
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid password!"));
    }

    @Test
        void login_UserNotFound() throws Exception {
        LoginDto dto = new LoginDto();
        dto.setUsername("nobody");
        dto.setPassword("hello123");

        when(userService.findByUsername("nobody"))
                .thenThrow(new UserNotFoundException("User not found: nobody"));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found: nobody"));
        }
}