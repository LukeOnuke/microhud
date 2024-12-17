package com.lukeonuke.minihud.service;

import com.lukeonuke.minihud.renderer.module.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class OptionsService {
    @Setter
    @Getter
    private ArrayList<String> enabledRenderers;
    @Setter
    @Getter
    private int schema;

    public boolean enabledPlayerDiscordTag;

    public OptionsService() {

    }

    public static OptionsService getDefault(){
        OptionsService optionsHolder = new OptionsService();
        optionsHolder.setSchema(2);
        ArrayList<String> renderers = new ArrayList<>();
        renderers.add(MHWeatherTemperatureAndConditionModule.class.getSimpleName());
        renderers.add(MHDateTimeRendererModule.class.getSimpleName());
        renderers.add(MHPlayerPositionRendererModule.class.getSimpleName());
        renderers.add(MHFpsRendererModule.class.getSimpleName());
        renderers.add(MHLookingAtRenderModule.class.getSimpleName());
        optionsHolder.setEnabledRenderers(renderers);
        optionsHolder.enabledPlayerDiscordTag = true;
        return optionsHolder;
    }
}
