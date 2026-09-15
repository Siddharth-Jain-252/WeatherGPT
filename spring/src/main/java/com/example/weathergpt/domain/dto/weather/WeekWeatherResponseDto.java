package com.example.weathergpt.domain.dto.weather;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WeekWeatherResponseDto(
        Location location,
        Current current,
        Forecast forecast,
        Alerts alerts
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
            Double visibilityKm,

            @JsonProperty("vis_miles")
            Double visibilityMiles,

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

    public record Forecast(
            @JsonProperty("forecastday")
            List<ForecastDay> forecastDays
    ) {}

    public record ForecastDay(
            String date,

            @JsonProperty("date_epoch")
            Long dateEpoch,

            Day day,
            Astro astro,

            List<Hour> hour
    ) {}

    public record Day(
            @JsonProperty("maxtemp_c")
            Double maxTempC,

            @JsonProperty("maxtemp_f")
            Double maxTempF,

            @JsonProperty("mintemp_c")
            Double minTempC,

            @JsonProperty("mintemp_f")
            Double minTempF,

            @JsonProperty("avgtemp_c")
            Double avgTempC,

            @JsonProperty("avgtemp_f")
            Double avgTempF,

            @JsonProperty("maxwind_mph")
            Double maxWindMph,

            @JsonProperty("maxwind_kph")
            Double maxWindKph,

            @JsonProperty("totalprecip_mm")
            Double totalPrecipMm,

            @JsonProperty("totalprecip_in")
            Double totalPrecipIn,

            @JsonProperty("totalsnow_cm")
            Double totalSnowCm,

            @JsonProperty("avgvis_km")
            Double avgVisibilityKm,

            @JsonProperty("avgvis_miles")
            Double avgVisibilityMiles,

            @JsonProperty("avghumidity")
            Integer avgHumidity,

            @JsonProperty("daily_will_it_rain")
            Integer dailyWillItRain,

            @JsonProperty("daily_chance_of_rain")
            Integer dailyChanceOfRain,

            @JsonProperty("daily_will_it_snow")
            Integer dailyWillItSnow,

            @JsonProperty("daily_chance_of_snow")
            Integer dailyChanceOfSnow,

            Condition condition,

            Double uv,

            @JsonProperty("avgwetbulb_c")
            Double avgWetBulbC,

            @JsonProperty("avgwetbulb_f")
            Double avgWetBulbF,

            @JsonProperty("maxwetbulb_c")
            Double maxWetBulbC,

            @JsonProperty("maxwetbulb_f")
            Double maxWetBulbF
    ) {}

    public record Astro(
            String sunrise,
            String sunset,
            String moonrise,
            String moonset,

            @JsonProperty("moon_phase")
            String moonPhase,

            @JsonProperty("moon_illumination")
            Integer moonIllumination,

            @JsonProperty("is_moon_up")
            Integer isMoonUp,

            @JsonProperty("is_sun_up")
            Integer isSunUp
    ) {}

    public record Hour(
            @JsonProperty("time_epoch")
            Long timeEpoch,

            String time,

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

            @JsonProperty("snow_cm")
            Double snowCm,

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

            @JsonProperty("will_it_rain")
            Integer willItRain,

            @JsonProperty("chance_of_rain")
            Integer chanceOfRain,

            @JsonProperty("will_it_snow")
            Integer willItSnow,

            @JsonProperty("chance_of_snow")
            Integer chanceOfSnow,

            @JsonProperty("vis_km")
            Double visibilityKm,

            @JsonProperty("vis_miles")
            Double visibilityMiles,

            @JsonProperty("gust_mph")
            Double gustMph,

            @JsonProperty("gust_kph")
            Double gustKph,

            Double uv,

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

    public record Alerts(
            List<Object> alert
    ) {}
}