package com.example.weathergpt.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.example.weathergpt.domain.dto.UserRequestDto;
import com.example.weathergpt.domain.dto.UserResponseDto;
import com.example.weathergpt.domain.dto.weather.AviationWeatherDto;
import com.example.weathergpt.domain.dto.weather.AviationWeatherResponseDto;
import com.example.weathergpt.domain.dto.weather.CurrentWeatherDto;
import com.example.weathergpt.domain.dto.weather.CurrentWeatherResponseDto;
import com.example.weathergpt.domain.dto.weather.DayWeatherDto;
import com.example.weathergpt.domain.dto.weather.DayWeatherResponseDto;
import com.example.weathergpt.domain.dto.weather.MarineWeatherDto;
import com.example.weathergpt.domain.dto.weather.MarineWeatherResponseDto;
import com.example.weathergpt.domain.dto.weather.NwpWeatherDto;
import com.example.weathergpt.domain.dto.weather.NwpWeatherResponseDto;
import com.example.weathergpt.domain.dto.weather.WeekWeatherDto;
import com.example.weathergpt.domain.dto.weather.WeekWeatherResponseDto;
import com.example.weathergpt.domain.entity.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WeatherMapper {
    
    CurrentWeatherDto toCurrentWeatherData(CurrentWeatherResponseDto currentWeatherResponseDto);

    DayWeatherDto toDayWeatherData(DayWeatherResponseDto dayWeatherResponseDto);
    
    WeekWeatherDto toWeekWeatherData(WeekWeatherResponseDto weekWeatherResponseDto);

    AviationWeatherDto toAviationWeatherData(AviationWeatherResponseDto aviationWeatherResponseDto);
    
    MarineWeatherDto toMarineWeatherData(MarineWeatherResponseDto marineWeatherResponseDto);

    NwpWeatherDto toNwpWeatherData(NwpWeatherResponseDto nwpWeatherResponseDto);

    UserResponseDto toUserResponseDto(User user);

    User toUserEntity(UserRequestDto userRequestDto);


}
