package com.example.weathergpt.controller;

import org.springframework.web.bind.annotation.*;

import com.example.weathergpt.domain.dto.llm.AlertRequestDto;
import com.example.weathergpt.domain.dto.llm.IntentRequestDto;
import com.example.weathergpt.domain.dto.llm.IntentResponseDto;
import com.example.weathergpt.domain.dto.llm.LlmChatRequestDto;
import com.example.weathergpt.domain.dto.llm.LlmChatResponseDto;
import com.example.weathergpt.service.LlmService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/llm")
@RequiredArgsConstructor
public class LlmController {

    private final LlmService llmService;

    @PostMapping("/intent")
    public IntentResponseDto detectIntent(
            @Valid @RequestBody IntentRequestDto request) {

        return llmService.detectIntent(request);
    }

    @PostMapping("/chat")
    public LlmChatResponseDto chat(
            @Valid @RequestBody LlmChatRequestDto request) {

        return llmService.generateChat(request);
    }

    @PostMapping("/alert")
    public LlmChatResponseDto alert(
            @Valid @RequestBody AlertRequestDto request) {

        return llmService.generateAlert(request);
    }
}