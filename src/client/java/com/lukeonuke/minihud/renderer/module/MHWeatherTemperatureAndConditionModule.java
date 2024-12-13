package com.lukeonuke.minihud.renderer.module;

import com.lukeonuke.minihud.data.type.WeatherResponse;
import com.lukeonuke.minihud.service.WeatherService;
import net.minecraft.util.Formatting;

public class MHWeatherTemperatureAndConditionModule implements MicroHudRendererModule {

    private final WeatherService weatherService = WeatherService.getInstance();

    @Override
    public String getName() {
        return "IRL weather temp and condition";
    }

    @Override
    public String render(float deltaTick) {
        if (!weatherService.isReady()) return "Fetching weather data...";
        WeatherResponse.CurrentWeather weather = weatherService.getWeather().getCurrent();
        return Math.round(WeatherService.kelvinToCelsius(weather.getTemp()))
                + "C " + Formatting.ITALIC + "( Feels Like: " + Math.round(WeatherService.kelvinToCelsius(weather.getFeels_like())) + "C ) " + Formatting.RESET
                + Formatting.BOLD + weather.getWeather().get(0).getDescription();
    }

    @Override
    public void onEnable(boolean enabled) {
        if (enabled) weatherService.refreshWeather();
    }
}
