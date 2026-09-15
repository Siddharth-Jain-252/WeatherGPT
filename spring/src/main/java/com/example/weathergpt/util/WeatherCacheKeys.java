package com.example.weathergpt.util;

import java.util.Locale;

public final class WeatherCacheKeys {

    private WeatherCacheKeys() {
    }


    public static String currentByCity(String city) {

        return "weather:current:city:" +
                normalizeCity(city);
    }


    public static String dayByCity(String city) {

        return "weather:day:city:" +
                normalizeCity(city);
    }


    public static String weekByCity(String city) {

        return "weather:week:city:" +
                normalizeCity(city);
    }


    public static String currentByCoordinates(
            Double latitude,
            Double longitude) {

        return String.format(
                Locale.ROOT,
                "weather:current:coord:%.4f:%.4f",
                latitude,
                longitude
        );
    }


    public static String dayByCoordinates(
            Double latitude,
            Double longitude) {

        return String.format(
                Locale.ROOT,
                "weather:day:coord:%.4f:%.4f",
                latitude,
                longitude
        );
    }


    public static String weekByCoordinates(
            Double latitude,
            Double longitude) {

        return String.format(
                Locale.ROOT,
                "weather:week:coord:%.4f:%.4f",
                latitude,
                longitude
        );
    }


    private static String normalizeCity(String city) {

        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException(
                    "City cannot be null or blank"
            );
        }

        return city
                .trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ");
    }
}