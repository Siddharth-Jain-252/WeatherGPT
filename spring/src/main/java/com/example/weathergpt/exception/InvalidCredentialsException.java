package com.example.weathergpt.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
