package com.example.weathergpt.exception;

public class EmailAlreadyExistsException extends WeatherGptException {

    public EmailAlreadyExistsException() {
    }

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
    
}
