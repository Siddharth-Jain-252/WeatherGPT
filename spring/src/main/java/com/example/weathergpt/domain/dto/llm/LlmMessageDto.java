package com.example.weathergpt.domain.dto.llm;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
public record LlmMessageDto(@JsonProperty("user_message") @NotBlank String userMessage,
                            @JsonProperty("llm_message") @NotBlank String llmMessage) {}
