package com.example.weathergpt.domain.dto.weather;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MarineWeatherDto(
    Location location,
        Forecast forecast
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

            @JsonProperty("avgvis_km")
            Double avgVisibilityKm,

            @JsonProperty("avgvis_miles")
            Double avgVisibilityMiles,

            @JsonProperty("avghumidity")
            Integer avgHumidity,

            Condition condition
    ) {}

    public record Condition(
            String text,
            String icon,
            Integer code
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

            @JsonProperty("humidity")
            Integer humidity,

            @JsonProperty("cloud")
            Integer cloud,

            @JsonProperty("vis_km")
            Double visibilityKm,

            @JsonProperty("vis_miles")
            Double visibilityMiles,

            @JsonProperty("gust_mph")
            Double gustMph,

            @JsonProperty("gust_kph")
            Double gustKph,

            Double uv,

            // Marine-specific fields

            @JsonProperty("water_temp_c")
            Double waterTempC,

            @JsonProperty("water_temp_f")
            Double waterTempF,

            @JsonProperty("sig_ht_mt")
            Double significantWaveHeightM,

            @JsonProperty("sig_ht_ft")
            Double significantWaveHeightFt,

            @JsonProperty("swell_ht_mt")
            Double swellHeightM,

            @JsonProperty("swell_ht_ft")
            Double swellHeightFt,

            @JsonProperty("swell_dir")
            String swellDirection,

            @JsonProperty("swell_dir_16_point")
            String swellDirection16Point,

            @JsonProperty("swell_period_secs")
            Double swellPeriodSeconds,

            @JsonProperty("wind_wave_ht_mt")
            Double windWaveHeightM,

            @JsonProperty("wind_wave_ht_ft")
            Double windWaveHeightF,

            @JsonProperty("wind_wave_dir")
            String windWaveDirection,

            @JsonProperty("wind_wave_dir_16_point")
            String windWaveDirection16Point,

            @JsonProperty("wind_wave_period_secs")
            Double windWavePeriodSeconds,

            @JsonProperty("wind_wave_height_mt")
            Double windWaveHeightMt,

            @JsonProperty("wind_wave_height_ft")
            Double windWaveHeightFt,

            @JsonProperty("wind_wave_period")
            Double windWavePeriod,

            @JsonProperty("wind_wave_direction")
            String windWaveDirectionLegacy,

            @JsonProperty("water_temp")
            Double waterTemp
    ) {}
}