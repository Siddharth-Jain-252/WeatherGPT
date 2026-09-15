package com.example.weathergpt.service;

import com.example.weathergpt.domain.dto.llm.AlertRequestDto;
import com.example.weathergpt.domain.dto.llm.IntentRequestDto;
import com.example.weathergpt.domain.dto.llm.IntentResponseDto;
import com.example.weathergpt.domain.dto.llm.LlmChatRequestDto;
import com.example.weathergpt.domain.dto.llm.LlmChatResponseDto;

public interface LlmService {

    IntentResponseDto detectIntent(
            IntentRequestDto request
    );

    LlmChatResponseDto generateChat(
            LlmChatRequestDto request
    );

    LlmChatResponseDto generateAlert(
            AlertRequestDto request
    );
}