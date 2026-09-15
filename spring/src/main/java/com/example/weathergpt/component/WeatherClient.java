package com.example.weathergpt.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import com.example.weathergpt.domain.dto.weather.*;
import lombok.RequiredArgsConstructor;

@Component @RequiredArgsConstructor
public class WeatherClient {
    private final RestClient weatherRestClient;
    @Value("${weather.api.key}") private String apiKey;

    public CurrentWeatherResponseDto getCurrentWeather(String city) {
        return weatherRestClient.get().uri(u -> u.path("/v1/current.json")
                .queryParam("key", apiKey).queryParam("q", city).queryParam("aqi","no").build())
                .retrieve().body(CurrentWeatherResponseDto.class);
    }
    public CurrentWeatherResponseDto getCurrentWeather(Double longitude, Double latitude) {
        return weatherRestClient.get().uri(u -> u.path("/v1/current.json")
                .queryParam("key", apiKey).queryParam("q", latitude+","+longitude).queryParam("aqi","no").build())
                .retrieve().body(CurrentWeatherResponseDto.class);
    }
    public DayWeatherResponseDto getDayWeather(String city) {
        return weatherRestClient.get().uri(u -> u.path("/v1/forecast.json")
                .queryParam("key",apiKey).queryParam("days",1).queryParam("q",city)
                .queryParam("aqi","no").queryParam("alerts","no").build())
                .retrieve().body(DayWeatherResponseDto.class);
    }
    public DayWeatherResponseDto getDayWeather(Double longitude, Double latitude) {
        return weatherRestClient.get().uri(u -> u.path("/v1/forecast.json")
                .queryParam("key",apiKey).queryParam("days",1).queryParam("q",latitude+","+longitude)
                .queryParam("aqi","no").queryParam("alerts","no").build())
                .retrieve().body(DayWeatherResponseDto.class);
    }
    public WeekWeatherResponseDto getWeekWeather(String city) {
        return weatherRestClient.get().uri(u -> u.path("/v1/forecast.json")
                .queryParam("key",apiKey).queryParam("days",7).queryParam("q",city)
                .queryParam("aqi","no").queryParam("alerts","no").build())
                .retrieve().body(WeekWeatherResponseDto.class);
    }
    public WeekWeatherResponseDto getWeekWeather(Double longitude, Double latitude) {
        return weatherRestClient.get().uri(u -> u.path("/v1/forecast.json")
                .queryParam("key",apiKey).queryParam("days",7).queryParam("q",latitude+","+longitude)
                .queryParam("aqi","no").queryParam("alerts","no").build())
                .retrieve().body(WeekWeatherResponseDto.class);
    }
}
