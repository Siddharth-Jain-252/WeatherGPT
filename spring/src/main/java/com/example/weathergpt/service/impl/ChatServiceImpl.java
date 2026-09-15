package com.example.weathergpt.service.impl;

import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.weathergpt.domain.dto.chat.*;
import com.example.weathergpt.domain.dto.llm.*;
import com.example.weathergpt.domain.dto.weather.*;
import com.example.weathergpt.domain.entity.*;
import com.example.weathergpt.exception.UserNotFoundException;
import com.example.weathergpt.repository.*;
import com.example.weathergpt.service.*;
import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final MessageService messageService;
    private final LlmService llmService;
    private final WeatherService weatherService;

    @Override @Transactional
    public ChatResponseDto chat(UUID userId, ChatRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Chat chat = chatRepository.findByUserId(userId).orElseGet(() -> {
            Chat created = Chat.builder().user(user).build();
            user.setChat(created);
            return chatRepository.save(created);
        });

        List<LlmMessageDto> history = messageService.getPastMessages(chat.getId()).stream()
                .map(m -> new LlmMessageDto(m.getUserMessage(), m.getLlmMessage()))
                .toList();

        IntentResponseDto intent = llmService.detectIntent(
                new IntentRequestDto(user.getRole(), history, request.query()));

        String location = resolveLocation(intent.location(), user);
        Map<String,Object> data = collectWeatherData(intent.requiredCapabilities(), location);

        LlmChatResponseDto answer = llmService.generateChat(
                new LlmChatRequestDto(user.getRole(), intent.intent(), request.query(), data));

        messageRepository.save(Message.builder()
                .chat(chat).userMessage(request.query()).llmMessage(answer.response()).build());

        return new ChatResponseDto(answer.response(), intent.intent(), location, intent.requiredCapabilities());
    }

    private String resolveLocation(String detectedLocation, User user) {
        if (detectedLocation != null && !detectedLocation.isBlank()) return detectedLocation.trim();
        if (user.getHomeLocation() != null && !user.getHomeLocation().isBlank()) return user.getHomeLocation().trim();
        return null;
    }

    private Map<String,Object> collectWeatherData(List<String> capabilities, String location) {
        Map<String,Object> data = new LinkedHashMap<>();
        if (location == null || location.isBlank()) return data;

        if (capabilities.contains("current_weather_data"))
            data.put("current_weather_data", weatherService.getCurrentWeatherData(location));
        if (capabilities.contains("day_weather_data"))
            data.put("day_weather_data", weatherService.getDayWeatherData(location));
        if (capabilities.contains("week_weather_data"))
            data.put("week_weather_data", weatherService.getWeekWeatherData(location));

        // No fake data: these are null until their real upstream clients are implemented.
        for (String capability : List.of("marine_weather_data","aviation_weather_data",
                                         "historical_weather_data","flight_details")) {
            if (capabilities.contains(capability)) data.put(capability, null);
        }
        return data;
    }
}
