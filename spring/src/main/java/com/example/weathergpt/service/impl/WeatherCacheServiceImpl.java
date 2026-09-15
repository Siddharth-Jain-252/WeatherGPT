package com.example.weathergpt.service.impl;

import java.time.Duration;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.weathergpt.domain.dto.weather.CurrentWeatherDto;
import com.example.weathergpt.domain.dto.weather.DayWeatherDto;
import com.example.weathergpt.domain.dto.weather.WeekWeatherDto;
import com.example.weathergpt.service.WeatherCacheService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherCacheServiceImpl implements WeatherCacheService {

    private final RedisTemplate<String, CurrentWeatherDto>
            currentWeatherRedisTemplate;

    private final RedisTemplate<String, DayWeatherDto>
            dayWeatherRedisTemplate;

    private final RedisTemplate<String, WeekWeatherDto>
            weekWeatherRedisTemplate;


    @Override
    public void saveCurrent(
            String key,
            CurrentWeatherDto data,
            Duration ttl) {

        try {
            currentWeatherRedisTemplate
                    .opsForValue()
                    .set(key, data, ttl);

        } catch (DataAccessException ex) {

            log.warn(
                    "Failed to save current weather to Redis. key={}",
                    key,
                    ex
            );
        }
    }


    @Override
    public CurrentWeatherDto getCurrent(String key) {

        try {
            return currentWeatherRedisTemplate
                    .opsForValue()
                    .get(key);

        } catch (DataAccessException ex) {

            log.warn(
                    "Failed to read current weather from Redis. key={}",
                    key,
                    ex
            );

            return null;
        }
    }


    @Override
    public void saveDay(
            String key,
            DayWeatherDto data,
            Duration ttl) {

        try {
            dayWeatherRedisTemplate
                    .opsForValue()
                    .set(key, data, ttl);

        } catch (DataAccessException ex) {

            log.warn(
                    "Failed to save day weather to Redis. key={}",
                    key,
                    ex
            );
        }
    }


    @Override
    public DayWeatherDto getDay(String key) {

        try {
            return dayWeatherRedisTemplate
                    .opsForValue()
                    .get(key);

        } catch (DataAccessException ex) {

            log.warn(
                    "Failed to read day weather from Redis. key={}",
                    key,
                    ex
            );

            return null;
        }
    }


    @Override
    public void saveWeek(
            String key,
            WeekWeatherDto data,
            Duration ttl) {

        try {
            weekWeatherRedisTemplate
                    .opsForValue()
                    .set(key, data, ttl);

        } catch (DataAccessException ex) {

            log.warn(
                    "Failed to save week weather to Redis. key={}",
                    key,
                    ex
            );
        }
    }


    @Override
    public WeekWeatherDto getWeek(String key) {

        try {
            return weekWeatherRedisTemplate
                    .opsForValue()
                    .get(key);

        } catch (DataAccessException ex) {

            log.warn(
                    "Failed to read week weather from Redis. key={}",
                    key,
                    ex
            );

            return null;
        }
    }


    @Override
    public void delete(String key) {

        try {

            currentWeatherRedisTemplate.delete(key);
            dayWeatherRedisTemplate.delete(key);
            weekWeatherRedisTemplate.delete(key);

        } catch (DataAccessException ex) {

            log.warn(
                    "Failed to delete weather cache. key={}",
                    key,
                    ex
            );
        }
    }


    @Override
    public void clearAllWeatherCache() {

        try {

            currentWeatherRedisTemplate
                    .delete(
                            currentWeatherRedisTemplate
                                    .keys("weather:*")
                    );

            dayWeatherRedisTemplate
                    .delete(
                            dayWeatherRedisTemplate
                                    .keys("weather:*")
                    );

            weekWeatherRedisTemplate
                    .delete(
                            weekWeatherRedisTemplate
                                    .keys("weather:*")
                    );

        } catch (DataAccessException ex) {

            log.warn(
                    "Failed to clear weather cache",
                    ex
            );
        }
    }
}