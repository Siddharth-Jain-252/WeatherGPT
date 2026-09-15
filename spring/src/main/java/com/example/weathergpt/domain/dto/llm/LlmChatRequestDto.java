package com.example.weathergpt.domain.dto.llm;
import java.util.Map;
import com.example.weathergpt.domain.entity.RoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public record LlmChatRequestDto(@NotNull RoleEnum speciality, @NotBlank String intent, @NotBlank String query, Map<String,Object> data) {}
