package com.example.weathergpt.domain.dto.chat;
import jakarta.validation.constraints.NotBlank;
public record ChatRequestDto(@NotBlank String query) {}
