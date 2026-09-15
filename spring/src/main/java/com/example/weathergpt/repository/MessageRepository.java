package com.example.weathergpt.repository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.weathergpt.domain.entity.Message;
public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByChatIdOrderBySentTimeDesc(UUID chatId, Pageable pageable);
}
