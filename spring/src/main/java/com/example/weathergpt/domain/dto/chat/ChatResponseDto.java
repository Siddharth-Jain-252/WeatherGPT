package com.example.weathergpt.domain.dto.chat;
import java.util.List;
public record ChatResponseDto(String response, String intent, String location, List<String> requiredCapabilities) {}
