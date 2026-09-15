package com.example.weathergpt.domain.dto;

import com.example.weathergpt.domain.entity.RoleEnum;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserResponseDto(

    @NotBlank 
    String username,

    @NotBlank
    @Email 
    String email,

    RoleEnum role,

    String homeLocation,
    
    String workLocation
) {}
