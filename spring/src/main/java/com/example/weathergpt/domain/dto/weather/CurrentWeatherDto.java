package com.example.weathergpt.domain.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrentWeatherDto(
        Location location,
        Current current
) {

    public record Location(
            String name,
            String region,
            String country,
            Double lat,
            Double lon,

            @JsonProperty("tz_id")
            String tzId,

            @JsonProperty("localtime_epoch")
            Long localtimeEpoch,

            String localtime
    ) {}

    public record Current(

            @JsonProperty("temp_c")
            Double tempC,

            @JsonProperty("temp_f")
            Double tempF,

            Condition condition,

            @JsonProperty("wind_kph")
            Double windKph,

            @JsonProperty("wind_degree")
            Integer windDegree,

            @JsonProperty("wind_dir")
            String windDir,

            @JsonProperty("pressure_mb")
            Double pressureMb,

            @JsonProperty("precip_mm")
            Double precipMm,

            @JsonProperty("precip_in")
            Double precipIn,

            Integer humidity,
            Integer cloud,

            @JsonProperty("feelslike_c")
            Double feelsLikeC,

            Double uv,
            
            @JsonProperty("will_it_rain")
            Integer willItRain,

            @JsonProperty("chance_of_rain")
            Integer chanceOfRain,

            @JsonProperty("will_it_snow")
            Integer willItSnow,

            @JsonProperty("chance_of_snow")
            Integer chanceOfSnow

    ) {}

    public record Condition(
            String text
            
    ) {}
}

