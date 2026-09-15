package com.example.weathergpt.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.weathergpt.domain.dto.ErrorDto;
import com.example.weathergpt.exception.EmailAlreadyExistsException;
import com.example.weathergpt.exception.UserNotFoundException;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice 
@Slf4j 
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorDto> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        log.error("Caught EmailAlreadyExistsException",e);
        ErrorDto errorDto = new ErrorDto("Email already exists.");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException e) {
        log.error("Caught UserNotFoundException",e);
        ErrorDto errorDto = new ErrorDto("User not found.");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Caught MethodArgumentNotValidException",e);
        
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        String errorMessage = fieldErrors
        .stream()
        .findFirst()
        .map(fieldError -> 
            fieldError.getField() + ": " + fieldError.getDefaultMessage())
            .orElse("Validation error ocurred");
            
        ErrorDto errorDto = new ErrorDto(errorMessage);
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolation(ConstraintViolationException e) {
        log.error("Caught ConstraintViolationException",e);
        
        String errorMessage = e.getConstraintViolations()
        .stream()
        .findFirst()
        .map(violation -> 
            violation.getPropertyPath() + ": " + violation.getMessage()
        ).orElse("Constraintviolation occurred");
        
        ErrorDto errorDto = new ErrorDto(errorMessage);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception e) {
        log.error("Caught exception",e);
        ErrorDto errorDto = new ErrorDto("An unknown error occurred");
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
