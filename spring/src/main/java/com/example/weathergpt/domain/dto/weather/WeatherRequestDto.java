package com.example.weathergpt.domain.dto.weather;

public record WeatherRequestDto(

    CurrentWeatherDto currentWeatherDto,
    DayWeatherDto dayWeatherDto,
    WeekWeatherDto weekWeatherDto,
    MarineWeatherDto marineWeatherDto,
    AviationWeatherDto aviationWeatherDto,
    NwpWeatherDto nwpWeatherDto
) {}
