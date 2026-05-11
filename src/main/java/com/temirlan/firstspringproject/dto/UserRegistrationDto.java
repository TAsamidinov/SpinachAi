package com.temirlan.firstspringproject.dto;

import lombok.Data;

@Data
public class UserRegistrationDto {
    private String username;
    private String password; // plain text, will be hashed in service
}