package com.example.weathergpt.domain.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record MarineWeatherResponseDto(
        Location location,
        Forecast forecast
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

            @JsonProperty("avgvis_km")
            double avgVisibilityKm,

            @JsonProperty("avgvis_miles")
            double avgVisibilityMiles,

            @JsonProperty("avghumidity")
            int avgHumidity,

            Condition condition
    ) {}

    public record Condition(
            String text,
            String icon,
            int code
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

            @JsonProperty("humidity")
            int humidity,

            @JsonProperty("cloud")
            int cloud,

            @JsonProperty("vis_km")
            double visibilityKm,

            @JsonProperty("vis_miles")
            double visibilityMiles,

            @JsonProperty("gust_mph")
            double gustMph,

            @JsonProperty("gust_kph")
            double gustKph,

            double uv,

            // Marine-specific fields

            @JsonProperty("water_temp_c")
            double waterTempC,

            @JsonProperty("water_temp_f")
            double waterTempF,

            @JsonProperty("sig_ht_mt")
            double significantWaveHeightM,

            @JsonProperty("sig_ht_ft")
            double significantWaveHeightFt,

            @JsonProperty("swell_ht_mt")
            double swellHeightM,

            @JsonProperty("swell_ht_ft")
            double swellHeightFt,

            @JsonProperty("swell_dir")
            String swellDirection,

            @JsonProperty("swell_dir_16_point")
            String swellDirection16Point,

            @JsonProperty("swell_period_secs")
            double swellPeriodSeconds,

            @JsonProperty("wind_wave_ht_mt")
            double windWaveHeightM,

            @JsonProperty("wind_wave_ht_ft")
            double windWaveHeightF,

            @JsonProperty("wind_wave_dir")
            String windWaveDirection,

            @JsonProperty("wind_wave_dir_16_point")
            String windWaveDirection16Point,

            @JsonProperty("wind_wave_period_secs")
            double windWavePeriodSeconds,

            @JsonProperty("wind_wave_height_mt")
            double windWaveHeightMt,

            @JsonProperty("wind_wave_height_ft")
            double windWaveHeightFt,

            @JsonProperty("wind_wave_period")
            double windWavePeriod,

            @JsonProperty("wind_wave_direction")
            String windWaveDirectionLegacy,

            @JsonProperty("water_temp")
            double waterTemp
    ) {}
}