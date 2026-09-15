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
            double lat,
            double lon,

            @JsonProperty("tz_id")
            String tzId,

            @JsonProperty("localtime_epoch")
            long localtimeEpoch,

            String localtime
    ) {}

    public record Current(

            @JsonProperty("temp_c")
            double tempC,

            @JsonProperty("temp_f")
            double tempF,

            Condition condition,

            @JsonProperty("wind_kph")
            double windKph,

            @JsonProperty("wind_degree")
            int windDegree,

            @JsonProperty("wind_dir")
            String windDir,

            @JsonProperty("pressure_mb")
            double pressureMb,

            @JsonProperty("precip_mm")
            double precipMm,

            @JsonProperty("precip_in")
            double precipIn,

            int humidity,
            int cloud,

            @JsonProperty("feelslike_c")
            double feelsLikeC,

            double uv,
            
            @JsonProperty("will_it_rain")
            int willItRain,

            @JsonProperty("chance_of_rain")
            int chanceOfRain,

            @JsonProperty("will_it_snow")
            int willItSnow,

            @JsonProperty("chance_of_snow")
            int chanceOfSnow

    ) {}

    public record Condition(
            String text
            
    ) {}
}

