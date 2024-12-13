package com.lukeonuke.minihud.renderer.module;

import com.lukeonuke.minihud.data.type.WeatherResponse;
import com.lukeonuke.minihud.service.WeatherService;
import net.minecraft.util.Formatting;

public class MHWeatherWindAndHumidityModule implements MicroHudRendererModule{
    private final WeatherService weatherService = WeatherService.getInstance();

    @Override
    public String getName() {
        return "IRL weather wind and humidity";
    }

    @Override
    public String render(float deltaTick) {
        if (!weatherService.isReady()) return "Fetching weather data...";
        WeatherResponse.CurrentWeather weather = weatherService.getWeather().getCurrent();
        return "Wind: " + Formatting.BOLD + weather.getWind_speed() + "m/s " + weather.getWindDirectionTranslated() + Formatting.RESET
                + " | Humidity: " + Formatting.BOLD + weather.getHumidity() + "%" + Formatting.RESET;
    }

    @Override
    public void onEnable(boolean enabled) {
        if (enabled) weatherService.refreshWeather();
    }
}
