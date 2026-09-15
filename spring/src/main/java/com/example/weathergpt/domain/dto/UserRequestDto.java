package com.example.weathergpt.domain.dto;

import com.example.weathergpt.domain.entity.RoleEnum;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDto(

    @NotBlank 
    @Size(min = 3, max = 20)
    String username,

    @NotBlank
    @Email 
    String email,

    RoleEnum role,

    String homeLocation,

    String workLocation,

    @NotBlank 
    @Size(min = 8, max = 20)
    String password
) {}
