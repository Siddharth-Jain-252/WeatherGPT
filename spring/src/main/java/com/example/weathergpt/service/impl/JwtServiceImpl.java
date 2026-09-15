package com.example.weathergpt.service.impl;

import java.time.Instant;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.example.weathergpt.domain.entity.User;
import com.example.weathergpt.service.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtEncoder jwtEncoder;

    @Override
    public String generateToken(User user) {

        Instant now = Instant.now();

        JwtClaimsSet claims =
                JwtClaimsSet.builder()
                        .issuer("weathergpt")
                        .issuedAt(now)
                        .expiresAt(
                                now.plusSeconds(3600)
                        )
                        .subject(
                                user.getId().toString()
                        )
                        .claim(
                                "email",
                                user.getEmail()
                        )
                        .claim(
                                "username",
                                user.getUsername()
                        )
                        .claim(
                                "role",
                                user.getRole().getValue()
                        )
                        .build();

        return jwtEncoder
                .encode(
                        JwtEncoderParameters.from(claims)
                )
                .getTokenValue();
    }
}