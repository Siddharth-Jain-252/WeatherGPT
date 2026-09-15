package com.example.weathergpt.domain.dto.llm;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IntentResponseDto(

        String intent,

        String location,

        @JsonProperty("required_capabilities")
        List<String> requiredCapabilities

) {}