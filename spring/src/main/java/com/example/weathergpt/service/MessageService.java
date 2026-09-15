package com.example.weathergpt.service;

import java.util.List;
import java.util.UUID;

import com.example.weathergpt.domain.entity.Message;

public interface MessageService {
    
    public List<Message> getPastMessages(
        UUID chatId
    );

}
