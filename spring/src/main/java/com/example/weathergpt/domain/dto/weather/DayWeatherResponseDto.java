package com.example.weathergpt.domain.dto.weather;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DayWeatherResponseDto(
        Location location,
        Current current,
        Forecast forecast,
        Alerts alerts
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
            double visibilityKm,

            @JsonProperty("vis_miles")
            double visibilityMiles,

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

    public record Forecast(
            @JsonProperty("forecastday")
            List<ForecastDay> forecastDays
    ) {}

    public record ForecastDay(
            String date,

            @JsonProperty("date_epoch")
            long dateEpoch,

            Day day,
            Astro astro,

            List<Hour> hour
    ) {}

    public record Day(
            @JsonProperty("maxtemp_c")
            double maxTempC,

            @JsonProperty("maxtemp_f")
            double maxTempF,

            @JsonProperty("mintemp_c")
            double minTempC,

            @JsonProperty("mintemp_f")
            double minTempF,

            @JsonProperty("avgtemp_c")
            double avgTempC,

            @JsonProperty("avgtemp_f")
            double avgTempF,

            @JsonProperty("maxwind_mph")
            double maxWindMph,

            @JsonProperty("maxwind_kph")
            double maxWindKph,

            @JsonProperty("totalprecip_mm")
            double totalPrecipMm,

            @JsonProperty("totalprecip_in")
            double totalPrecipIn,

            @JsonProperty("totalsnow_cm")
            double totalSnowCm,

            @JsonProperty("avgvis_km")
            double avgVisibilityKm,

            @JsonProperty("avgvis_miles")
            double avgVisibilityMiles,

            @JsonProperty("avghumidity")
            int avgHumidity,

            @JsonProperty("daily_will_it_rain")
            int dailyWillItRain,

            @JsonProperty("daily_chance_of_rain")
            int dailyChanceOfRain,

            @JsonProperty("daily_will_it_snow")
            int dailyWillItSnow,

            @JsonProperty("daily_chance_of_snow")
            int dailyChanceOfSnow,

            Condition condition,

            double uv,

            @JsonProperty("avgwetbulb_c")
            double avgWetBulbC,

            @JsonProperty("avgwetbulb_f")
            double avgWetBulbF,

            @JsonProperty("maxwetbulb_c")
            double maxWetBulbC,

            @JsonProperty("maxwetbulb_f")
            double maxWetBulbF
    ) {}

    public record Astro(
            String sunrise,
            String sunset,
            String moonrise,
            String moonset,

            @JsonProperty("moon_phase")
            String moonPhase,

            @JsonProperty("moon_illumination")
            int moonIllumination,

            @JsonProperty("is_moon_up")
            int isMoonUp,

            @JsonProperty("is_sun_up")
            int isSunUp
    ) {}

    public record Hour(
            @JsonProperty("time_epoch")
            long timeEpoch,

            String time,

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

            @JsonProperty("snow_cm")
            double snowCm,

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

            @JsonProperty("will_it_rain")
            int willItRain,

            @JsonProperty("chance_of_rain")
            int chanceOfRain,

            @JsonProperty("will_it_snow")
            int willItSnow,

            @JsonProperty("chance_of_snow")
            int chanceOfSnow,

            @JsonProperty("vis_km")
            double visibilityKm,

            @JsonProperty("vis_miles")
            double visibilityMiles,

            @JsonProperty("gust_mph")
            double gustMph,

            @JsonProperty("gust_kph")
            double gustKph,

            double uv,

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

    public record Alerts(
            List<Object> alert
    ) {}
}