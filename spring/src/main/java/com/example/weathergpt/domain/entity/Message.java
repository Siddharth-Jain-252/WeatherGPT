package com.example.weathergpt.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "messages")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
public class Message {
    @Id @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @Column(name = "user_message", nullable = false, columnDefinition = "text") private String userMessage;
    @Column(name = "llm_message", nullable = false, columnDefinition = "text") private String llmMessage;
    @Column(name = "sent_time", nullable = false, updatable = false)
    @Builder.Default private LocalDateTime sentTime = LocalDateTime.now();
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;
}
