package com.example.weathergpt.service;
import java.util.UUID;
import com.example.weathergpt.domain.dto.chat.*;
public interface ChatService { ChatResponseDto chat(UUID userId, ChatRequestDto request); }
