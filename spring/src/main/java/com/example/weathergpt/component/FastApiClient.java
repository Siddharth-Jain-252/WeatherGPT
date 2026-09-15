package com.example.weathergpt.component;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.example.weathergpt.config.FastApiProperties;
import com.example.weathergpt.domain.dto.llm.AlertRequestDto;
import com.example.weathergpt.domain.dto.llm.IntentRequestDto;
import com.example.weathergpt.domain.dto.llm.IntentResponseDto;
import com.example.weathergpt.domain.dto.llm.LlmChatRequestDto;
import com.example.weathergpt.domain.dto.llm.LlmChatResponseDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FastApiClient {

    private final RestClient fastApiRestClient;
    private final FastApiProperties properties;

    public IntentResponseDto detectIntent(
            IntentRequestDto request) {

        return fastApiRestClient
                .post()
                .uri("/chat/intent")
                .header("api_key", properties.apiKey())
                .body(request)
                .retrieve()
                .body(IntentResponseDto.class);
    }

    public LlmChatResponseDto generateChat(
            LlmChatRequestDto request) {

        return fastApiRestClient
                .post()
                .uri("/chat")
                .header("api_key", properties.apiKey())
                .body(request)
                .retrieve()
                .body(LlmChatResponseDto.class);
    }

    public LlmChatResponseDto generateAlert(
            AlertRequestDto request) {

        return fastApiRestClient
                .post()
                .uri("/chat/alert")
                .header("api_key", properties.apiKey())
                .body(request)
                .retrieve()
                .body(LlmChatResponseDto.class);
    }
}