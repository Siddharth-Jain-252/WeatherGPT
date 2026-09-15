package com.example.weathergpt.service;

import java.time.Duration;

import com.example.weathergpt.domain.dto.weather.CurrentWeatherDto;
import com.example.weathergpt.domain.dto.weather.DayWeatherDto;
import com.example.weathergpt.domain.dto.weather.WeekWeatherDto;

public interface WeatherCacheService {

    void saveCurrent(
            String key,
            CurrentWeatherDto data,
            Duration ttl
    );

    CurrentWeatherDto getCurrent(
            String key
    );

    void saveDay(
            String key,
            DayWeatherDto data,
            Duration ttl
    );

    DayWeatherDto getDay(
            String key
    );

    void saveWeek(
            String key,
            WeekWeatherDto data,
            Duration ttl
    );

    WeekWeatherDto getWeek(
            String key
    );

    void delete(String key);

    void clearAllWeatherCache();
}