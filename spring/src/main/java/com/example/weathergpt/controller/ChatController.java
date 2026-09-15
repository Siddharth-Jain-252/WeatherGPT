package com.example.weathergpt.controller;
import java.util.UUID;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import com.example.weathergpt.domain.dto.chat.*;
import com.example.weathergpt.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController @RequestMapping("/api/v1/chat") @RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    @PostMapping
    public ChatResponseDto chat(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody ChatRequestDto request) {
        return chatService.chat(UUID.fromString(jwt.getSubject()), request);
    }
}
