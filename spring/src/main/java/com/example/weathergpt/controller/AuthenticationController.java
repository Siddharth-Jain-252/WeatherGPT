package com.example.weathergpt.controller;

import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.example.weathergpt.domain.dto.AuthResponseDto;
import com.example.weathergpt.domain.dto.LoginUserDto;
import com.example.weathergpt.domain.dto.UserRequestDto;
import com.example.weathergpt.domain.dto.UserResponseDto;
import com.example.weathergpt.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;


    @PostMapping("/register")
    public UserResponseDto registerUser(
            @Valid @RequestBody UserRequestDto request) {

        return userService.createUser(request);
    }


    @PostMapping("/login")
    public AuthResponseDto authenticateUser(
            @Valid @RequestBody LoginUserDto request) {

        return userService.authenticateUser(request);
    }


    @PostMapping("/update-password")
    public void updatePassword(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody LoginUserDto request) {

        UUID userId =
                UUID.fromString(jwt.getSubject());

        userService.updatePassword(
                userId,
                request.password()
        );
    }


    @DeleteMapping("/user")
    public void deleteUser(
            @AuthenticationPrincipal Jwt jwt) {

        UUID userId =
                UUID.fromString(jwt.getSubject());

        userService.deleteUser(userId);
    }


    @GetMapping("/profile")
    public UserResponseDto getUserProfile(
            @AuthenticationPrincipal Jwt jwt) {

        UUID userId =
                UUID.fromString(jwt.getSubject());

        return userService.getUserProfile(userId);
    }
}