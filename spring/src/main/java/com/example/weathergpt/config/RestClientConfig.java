package com.example.weathergpt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient weatherRestClient() {
        return RestClient.builder()
                .baseUrl("http://api.weatherapi.com")
                .build();
    }

    @Bean
    public RestClient fastApiRestClient(
            @Value("${fastapi.base-url}") String baseUrl) {

        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}