package com.example.weathergpt.domain.dto;

public record AuthResponseDto(
        String token,
        UserResponseDto user
) {}