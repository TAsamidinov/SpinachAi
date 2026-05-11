package com.temirlan.firstspringproject.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String username;
    private String password; // plain text, will be compared against hash
}