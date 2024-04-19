package com.lukeonuke.minihud.service;

import com.google.gson.Gson;
import com.lukeonuke.minihud.MicroHud;
import com.lukeonuke.minihud.data.type.GeolocationResponse;
import com.lukeonuke.minihud.data.type.WeatherResponse;
import com.lukeonuke.minihud.renderer.MicroHudRenderer;
import com.lukeonuke.minihud.renderer.module.MHWeatherTemperatureAndConditionModule;
import com.lukeonuke.minihud.renderer.module.MHWeatherWindAndHumidityModule;
import lombok.Getter;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class WeatherService {
    private static WeatherService instance = null;
    HttpService httpService = HttpService.getInstance();
    private final Gson gson = new Gson();
    @Getter
    private WeatherResponse weather;
    @Getter
    private boolean isReady = false;

    private WeatherService() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           @Override
                           public void run() {
                               refreshWeather();
                           }
                       }, 0L, 1200000L
        );
    }

    public static WeatherService getInstance() {
        if (instance == null) instance = new WeatherService();
        return instance;
    }

    public void refreshWeather() {
        MicroHudRenderer microHudRenderer = MicroHudRenderer.getInstance();
        if(microHudRenderer.isModuleEnabled(MHWeatherTemperatureAndConditionModule.class) || microHudRenderer.isModuleEnabled(MHWeatherWindAndHumidityModule.class) ) new Thread(() -> {
            try {
                GeolocationResponse geolocationResponse = gson.fromJson(httpService.get("https://ipapi.co/json/"), GeolocationResponse.class);
                weather =
                        gson.fromJson(
                                httpService.get(
                                        "https://api.pequla.com/weather?lat=" + geolocationResponse.getLatitude()
                                                + "&lon=" + geolocationResponse.getLongitude()
                                ), WeatherResponse.class);
                weather.getCurrent().setWindDirectionTranslated(angleToDirection(weather.getCurrent().getWind_deg()));
                isReady = true;
            } catch (IOException | InterruptedException e) {
                MicroHud.LOGGER.warn("Failed to fetch weather: " + e.getMessage()  + ". You can probably disregard this message.");
            }
        }).start();
    }

    public static double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15D;
    }

    private String angleToDirection(double angle) {
        angle = Math.abs(angle - 25.5D);
        return switch (((int) angle) / 45) {
            case 1 -> "N";
            case 2 -> "NE";
            case 3 -> "E";
            case 4 -> "SE";
            case 5 -> "S";
            case 6 -> "SW";
            case 7 -> "W";
            case 8 -> "NW";
            default -> "N/A";
        };
    }
}
