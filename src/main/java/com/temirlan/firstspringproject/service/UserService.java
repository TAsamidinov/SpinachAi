package com.temirlan.firstspringproject.service;

import com.temirlan.firstspringproject.dto.UserRegistrationDto;
import com.temirlan.firstspringproject.model.User;

public interface UserService {
    // job description — what any UserService must be able to do
    User registerUser(UserRegistrationDto dto);
    User findByUsername(String username);
}