package com.example.weathergpt.service.impl;

import java.time.Duration;

import org.springframework.stereotype.Service;

import com.example.weathergpt.component.WeatherClient;
import com.example.weathergpt.domain.dto.weather.AviationWeatherDto;
import com.example.weathergpt.domain.dto.weather.CurrentWeatherDto;
import com.example.weathergpt.domain.dto.weather.DayWeatherDto;
import com.example.weathergpt.domain.dto.weather.MarineWeatherDto;
import com.example.weathergpt.domain.dto.weather.NwpWeatherDto;
import com.example.weathergpt.domain.dto.weather.WeekWeatherDto;
import com.example.weathergpt.mapper.WeatherMapper;
import com.example.weathergpt.service.WeatherCacheService;
import com.example.weathergpt.service.WeatherService;
import com.example.weathergpt.util.WeatherCacheKeys;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final WeatherClient weatherClient;
    private final WeatherCacheService cacheService;
    private final WeatherMapper weatherMapper;


    private static final Duration CURRENT_TTL =
            Duration.ofMinutes(15);

    private static final Duration DAY_TTL =
            Duration.ofMinutes(30);

    private static final Duration WEEK_TTL =
            Duration.ofHours(1);


    @Override
    public CurrentWeatherDto getCurrentWeatherData(
            String city) {

        String key =
                WeatherCacheKeys.currentByCity(city);

        CurrentWeatherDto cached =
                cacheService.getCurrent(key);

        if (cached != null) {
            return cached;
        }

        CurrentWeatherDto weather =
                weatherMapper.toCurrentWeatherData(
                        weatherClient.getCurrentWeather(city)
                );

        cacheService.saveCurrent(
                key,
                weather,
                CURRENT_TTL
        );

        return weather;
    }


    @Override
    public CurrentWeatherDto getCurrentWeatherData(
            Double longitude,
            Double latitude) {

        String key =
                WeatherCacheKeys.currentByCoordinates(
                        latitude,
                        longitude
                );

        CurrentWeatherDto cached =
                cacheService.getCurrent(key);

        if (cached != null) {
            return cached;
        }

        CurrentWeatherDto weather =
                weatherMapper.toCurrentWeatherData(
                        weatherClient.getCurrentWeather(
                                longitude,
                                latitude
                        )
                );

        cacheService.saveCurrent(
                key,
                weather,
                CURRENT_TTL
        );

        return weather;
    }


    @Override
    public DayWeatherDto getDayWeatherData(
            String city) {

        String key =
                WeatherCacheKeys.dayByCity(city);

        DayWeatherDto cached =
                cacheService.getDay(key);

        if (cached != null) {
            return cached;
        }

        DayWeatherDto weather =
                weatherMapper.toDayWeatherData(
                        weatherClient.getDayWeather(city)
                );

        cacheService.saveDay(
                key,
                weather,
                DAY_TTL
        );

        return weather;
    }


    @Override
    public DayWeatherDto getDayWeatherData(
            Double longitude,
            Double latitude) {

        String key =
                WeatherCacheKeys.dayByCoordinates(
                        latitude,
                        longitude
                );

        DayWeatherDto cached =
                cacheService.getDay(key);

        if (cached != null) {
            return cached;
        }

        DayWeatherDto weather =
                weatherMapper.toDayWeatherData(
                        weatherClient.getDayWeather(
                                longitude,
                                latitude
                        )
                );

        cacheService.saveDay(
                key,
                weather,
                DAY_TTL
        );

        return weather;
    }


    @Override
    public WeekWeatherDto getWeekWeatherData(
            String city) {

        String key =
                WeatherCacheKeys.weekByCity(city);

        WeekWeatherDto cached =
                cacheService.getWeek(key);

        if (cached != null) {
            return cached;
        }

        WeekWeatherDto weather =
                weatherMapper.toWeekWeatherData(
                        weatherClient.getWeekWeather(city)
                );

        cacheService.saveWeek(
                key,
                weather,
                WEEK_TTL
        );

        return weather;
    }


    @Override
    public WeekWeatherDto getWeekWeatherData(
            Double longitude,
            Double latitude) {

        String key =
                WeatherCacheKeys.weekByCoordinates(
                        latitude,
                        longitude
                );

        WeekWeatherDto cached =
                cacheService.getWeek(key);

        if (cached != null) {
            return cached;
        }

        WeekWeatherDto weather =
                weatherMapper.toWeekWeatherData(
                        weatherClient.getWeekWeather(
                                longitude,
                                latitude
                        )
                );

        cacheService.saveWeek(
                key,
                weather,
                WEEK_TTL
        );

        return weather;
    }

    @Override
    public NwpWeatherDto getNwpWeatherData() {
        return null;
    }

    @Override
    public AviationWeatherDto getAviationWeatherData() {
        return null;
    }

    @Override
    public MarineWeatherDto getMarineWeatherData() {
        return null;
    }
}