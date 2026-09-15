package com.example.weathergpt.domain.dto.llm;

import com.example.weathergpt.domain.entity.RoleEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AlertRequestDto(

        @NotNull
        RoleEnum speciality,

        @NotBlank
        String alert

) {}