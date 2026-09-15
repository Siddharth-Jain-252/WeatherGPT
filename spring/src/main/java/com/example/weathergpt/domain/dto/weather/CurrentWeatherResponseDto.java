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
            double lat,
            double lon,

            @JsonProperty("tz_id")
            String tzId,

            String localtime
    ) {}

    public record Current(

            @JsonProperty("last_updated_epoch")
            long lastUpdatedEpoch,

            @JsonProperty("last_updated")
            String lastUpdated,

            @JsonProperty("temp_c")
            double tempC,

            @JsonProperty("temp_f")
            double tempF,

            @JsonProperty("is_day")
            int isDay,

            Condition condition,

            @JsonProperty("wind_mph")
            double windMph,

            @JsonProperty("wind_kph")
            double windKph,

            @JsonProperty("wind_degree")
            int windDegree,

            @JsonProperty("wind_dir")
            String windDir,

            @JsonProperty("pressure_mb")
            double pressureMb,

            @JsonProperty("pressure_in")
            double pressureIn,

            @JsonProperty("precip_mm")
            double precipMm,

            @JsonProperty("precip_in")
            double precipIn,

            int humidity,
            int cloud,

            @JsonProperty("feelslike_c")
            double feelsLikeC,

            @JsonProperty("feelslike_f")
            double feelsLikeF,

            @JsonProperty("windchill_c")
            double windChillC,

            @JsonProperty("windchill_f")
            double windChillF,

            @JsonProperty("heatindex_c")
            double heatIndexC,

            @JsonProperty("heatindex_f")
            double heatIndexF,

            @JsonProperty("dewpoint_c")
            double dewPointC,

            @JsonProperty("dewpoint_f")
            double dewPointF,

            @JsonProperty("vis_km")
            double visKm,

            @JsonProperty("vis_miles")
            double visMiles,

            double uv,

            @JsonProperty("gust_mph")
            double gustMph,

            @JsonProperty("gust_kph")
            double gustKph,

            @JsonProperty("will_it_rain")
            int willItRain,

            @JsonProperty("chance_of_rain")
            int chanceOfRain,

            @JsonProperty("will_it_snow")
            int willItSnow,

            @JsonProperty("chance_of_snow")
            int chanceOfSnow,

            @JsonProperty("wetbulb_c")
            double wetBulbC,

            @JsonProperty("wetbulb_f")
            double wetBulbF,

            @JsonProperty("short_rad")
            double shortRad,

            @JsonProperty("diff_rad")
            double diffRad,

            double dni,
            double gti
    ) {}

    public record Condition(
            String text,
            String icon,
            int code
    ) {}
}
