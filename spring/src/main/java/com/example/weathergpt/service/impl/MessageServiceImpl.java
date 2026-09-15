package com.example.weathergpt.service.impl;
import java.util.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.example.weathergpt.domain.entity.Message;
import com.example.weathergpt.repository.MessageRepository;
import com.example.weathergpt.service.MessageService;
import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    @Override public List<Message> getPastMessages(UUID chatId) {
        List<Message> messages = messageRepository.findByChatIdOrderBySentTimeDesc(chatId, PageRequest.of(0, 10));
        Collections.reverse(messages);
        return messages;
    }
}
