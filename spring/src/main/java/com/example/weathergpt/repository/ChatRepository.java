package com.example.weathergpt.repository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.weathergpt.domain.entity.Chat;
public interface ChatRepository extends JpaRepository<Chat, UUID> {
    Optional<Chat> findByUserId(UUID userId);
}
