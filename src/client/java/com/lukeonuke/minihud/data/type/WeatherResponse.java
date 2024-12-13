package com.lukeonuke.minihud.data.type;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class WeatherResponse {
    @Getter
    public static class CurrentWeather {
        double temp;
        double feels_like;
        double humidity;
        double wind_speed;
        double wind_deg;
        @Setter
        String windDirectionTranslated;
        List<Weather> weather;
    }
    @Getter
    public static class Weather {
        String description;
    }

    CurrentWeather current;
}
