package com.example.weathergpt.domain.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrentWeatherResponseDto(
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

            String localtime
    ) {}

    public record Current(

            @JsonProperty("last_updated_epoch")
            Long lastUpdatedEpoch,

            @JsonProperty("last_updated")
            String lastUpdated,

            @JsonProperty("temp_c")
            Double tempC,

            @JsonProperty("temp_f")
            Double tempF,

            @JsonProperty("is_day")
            Integer isDay,

            Condition condition,

            @JsonProperty("wind_mph")
            Double windMph,

            @JsonProperty("wind_kph")
            Double windKph,

            @JsonProperty("wind_degree")
            Integer windDegree,

            @JsonProperty("wind_dir")
            String windDir,

            @JsonProperty("pressure_mb")
            Double pressureMb,

            @JsonProperty("pressure_in")
            Double pressureIn,

            @JsonProperty("precip_mm")
            Double precipMm,

            @JsonProperty("precip_in")
            Double precipIn,

            Integer humidity,
            Integer cloud,

            @JsonProperty("feelslike_c")
            Double feelsLikeC,

            @JsonProperty("feelslike_f")
            Double feelsLikeF,

            @JsonProperty("windchill_c")
            Double windChillC,

            @JsonProperty("windchill_f")
            Double windChillF,

            @JsonProperty("heatindex_c")
            Double heatIndexC,

            @JsonProperty("heatindex_f")
            Double heatIndexF,

            @JsonProperty("dewpoint_c")
            Double dewPointC,

            @JsonProperty("dewpoint_f")
            Double dewPointF,

            @JsonProperty("vis_km")
            Double visKm,

            @JsonProperty("vis_miles")
            Double visMiles,

            Double uv,

            @JsonProperty("gust_mph")
            Double gustMph,

            @JsonProperty("gust_kph")
            Double gustKph,

            @JsonProperty("will_it_rain")
            Integer willItRain,

            @JsonProperty("chance_of_rain")
            Integer chanceOfRain,

            @JsonProperty("will_it_snow")
            Integer willItSnow,

            @JsonProperty("chance_of_snow")
            Integer chanceOfSnow,

            @JsonProperty("wetbulb_c")
            Double wetBulbC,

            @JsonProperty("wetbulb_f")
            Double wetBulbF,

            @JsonProperty("short_rad")
            Double shortRad,

            @JsonProperty("diff_rad")
            Double diffRad,

            Double dni,
            Double gti
    ) {}

    public record Condition(
            String text,
            String icon,
            Integer code
    ) {}
}
