package com.example.weathergpt.service;

import com.example.weathergpt.domain.entity.User;

public interface JwtService {

    public String generateToken(User user);

}
