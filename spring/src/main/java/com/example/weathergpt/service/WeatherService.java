package com.example.weathergpt.service;

import com.example.weathergpt.domain.dto.weather.AviationWeatherDto;
import com.example.weathergpt.domain.dto.weather.CurrentWeatherDto;
import com.example.weathergpt.domain.dto.weather.DayWeatherDto;
import com.example.weathergpt.domain.dto.weather.MarineWeatherDto;
import com.example.weathergpt.domain.dto.weather.NwpWeatherDto;
import com.example.weathergpt.domain.dto.weather.WeekWeatherDto;

public interface WeatherService {

    public CurrentWeatherDto getCurrentWeatherData(
        String city
    );

    public CurrentWeatherDto getCurrentWeatherData(
        Double longitude,
        Double latitude
    );

    public DayWeatherDto getDayWeatherData(
        String city
    );

    public DayWeatherDto getDayWeatherData(
        Double longitude,
        Double latitude
    );

    public WeekWeatherDto getWeekWeatherData(
        String city
    );

    public WeekWeatherDto getWeekWeatherData(
        Double longitude,
        Double latitude
    );

    public MarineWeatherDto getMarineWeatherData();

    public AviationWeatherDto getAviationWeatherData();
  
    public NwpWeatherDto getNwpWeatherData();

}
