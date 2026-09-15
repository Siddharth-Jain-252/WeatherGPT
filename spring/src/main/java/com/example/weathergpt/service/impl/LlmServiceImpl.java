package com.example.weathergpt.service.impl;

import org.springframework.stereotype.Service;

import com.example.weathergpt.component.FastApiClient;
import com.example.weathergpt.domain.dto.llm.AlertRequestDto;
import com.example.weathergpt.domain.dto.llm.IntentRequestDto;
import com.example.weathergpt.domain.dto.llm.IntentResponseDto;
import com.example.weathergpt.domain.dto.llm.LlmChatRequestDto;
import com.example.weathergpt.domain.dto.llm.LlmChatResponseDto;
import com.example.weathergpt.service.LlmService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LlmServiceImpl implements LlmService {

    private final FastApiClient fastApiClient;

    @Override
    public IntentResponseDto detectIntent(
            IntentRequestDto request) {

        return fastApiClient.detectIntent(request);
    }

    @Override
    public LlmChatResponseDto generateChat(
            LlmChatRequestDto request) {

        return fastApiClient.generateChat(request);
    }

    @Override
    public LlmChatResponseDto generateAlert(
            AlertRequestDto request) {

        return fastApiClient.generateAlert(request);
    }
}