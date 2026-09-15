package com.example.weathergpt.domain.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "chats")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
public class Chat {
    @Id @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default private List<Message> messages = new ArrayList<>();
}
