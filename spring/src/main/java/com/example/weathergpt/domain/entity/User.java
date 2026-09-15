package com.example.weathergpt.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
public class User {
    @Id @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "username", nullable = false) private String username;
    @Column(name = "email", nullable = false, unique = true) private String email;
    @Column(name = "role", nullable = false) @Enumerated(EnumType.STRING) private RoleEnum role;
    @Column(name = "home_location") private String homeLocation;
    @Column(name = "work_location") private String workLocation;
    @Column(name = "password") private String password;
    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default private LocalDateTime createdAt = LocalDateTime.now();
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Chat chat;
}
