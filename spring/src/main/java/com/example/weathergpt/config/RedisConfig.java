package com.example.weathergpt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.example.weathergpt.domain.dto.weather.CurrentWeatherDto;
import com.example.weathergpt.domain.dto.weather.DayWeatherDto;
import com.example.weathergpt.domain.dto.weather.WeekWeatherDto;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, CurrentWeatherDto> currentWeatherRedisTemplate(
            RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, CurrentWeatherDto> template =
                new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(
                new StringRedisSerializer()
        );

        template.setValueSerializer(
                new JacksonJsonRedisSerializer<>(
                        CurrentWeatherDto.class
                )
        );

        template.afterPropertiesSet();

        return template;
    }

    @Bean
    public RedisTemplate<String, DayWeatherDto> dayWeatherRedisTemplate(
            RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, DayWeatherDto> template =
                new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(
                new StringRedisSerializer()
        );

        template.setValueSerializer(
                new JacksonJsonRedisSerializer<>(
                        DayWeatherDto.class
                )
        );

        template.afterPropertiesSet();

        return template;
    }

    @Bean
    public RedisTemplate<String, WeekWeatherDto> weekWeatherRedisTemplate(
            RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, WeekWeatherDto> template =
                new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(
                new StringRedisSerializer()
        );

        template.setValueSerializer(
                new JacksonJsonRedisSerializer<>(
                        WeekWeatherDto.class
                )
        );

        template.afterPropertiesSet();

        return template;
    }
}