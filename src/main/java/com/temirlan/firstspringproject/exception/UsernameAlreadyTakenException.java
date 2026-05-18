package com.temirlan.firstspringproject.exception;

public class UsernameAlreadyTakenException extends RuntimeException {
    public UsernameAlreadyTakenException(String message) {
        super(message);
    }
}