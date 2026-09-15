package com.example.weathergpt.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FastApiProperties.class)
public class FastApiConfig {
}