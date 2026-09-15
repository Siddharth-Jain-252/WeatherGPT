package com.example.weathergpt.domain.dto.llm;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.example.weathergpt.domain.entity.RoleEnum;

public record IntentRequestDto(

        @NotNull
        RoleEnum speciality,

        @JsonProperty("past_messages")
        @Valid
        List<LlmMessageDto> pastMessages,

        @NotBlank
        String query

) {}