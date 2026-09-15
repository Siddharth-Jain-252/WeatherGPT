package com.example.weathergpt.service;

import java.util.UUID;

import com.example.weathergpt.domain.dto.AuthResponseDto;
import com.example.weathergpt.domain.dto.LoginUserDto;
import com.example.weathergpt.domain.dto.UserRequestDto;
import com.example.weathergpt.domain.dto.UserResponseDto;

public interface UserService {

    UserResponseDto createUser(
            UserRequestDto userRequestDto
    );

    AuthResponseDto authenticateUser(
            LoginUserDto loginUserDto
    );

    void deleteUser(UUID userId);

    void updatePassword(
            UUID userId,
            String newPassword
    );

    UserResponseDto getUserProfile(
            UUID userId
    );
}