package com.example.weathergpt.exception;

public class UserNotFoundException extends WeatherGptException {

    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
    }

}