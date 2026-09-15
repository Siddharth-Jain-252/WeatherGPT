package com.example.weathergpt.controller;
import org.springframework.web.bind.annotation.*;
import com.example.weathergpt.domain.dto.weather.*;
import com.example.weathergpt.service.WeatherService;
import lombok.RequiredArgsConstructor;

@RestController @RequestMapping("/api/v1/weather") @RequiredArgsConstructor
public class WeatherController {
    private final WeatherService weatherService;
    @GetMapping("/test") public String test() { return "WeatherController is working!"; }
    @GetMapping("/current/city") public CurrentWeatherDto current(@RequestParam String city) { return weatherService.getCurrentWeatherData(city); }
    @GetMapping("/current/coor") public CurrentWeatherDto current(@RequestParam Double longitude,@RequestParam Double latitude) { return weatherService.getCurrentWeatherData(longitude,latitude); }
    @GetMapping("/day/city") public DayWeatherDto day(@RequestParam String city) { return weatherService.getDayWeatherData(city); }
    @GetMapping("/day/coor") public DayWeatherDto day(@RequestParam Double longitude,@RequestParam Double latitude) { return weatherService.getDayWeatherData(longitude,latitude); }
    @GetMapping("/week/city") public WeekWeatherDto week(@RequestParam String city) { return weatherService.getWeekWeatherData(city); }
    @GetMapping("/week/coor") public WeekWeatherDto week(@RequestParam Double longitude,@RequestParam Double latitude) { return weatherService.getWeekWeatherData(longitude,latitude); }
}
