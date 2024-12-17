package com.lukeonuke.minihud.renderer;

import com.lukeonuke.minihud.MicroHudColors;
import com.lukeonuke.minihud.data.MicroHudOptions;
import com.lukeonuke.minihud.gui.MHGuiUtil;
import com.lukeonuke.minihud.renderer.module.*;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.Objects;

public class MicroHudRenderer {
    private final MinecraftClient client = MinecraftClient.getInstance();

    private MicroHudRenderer() {
        final ArrayList<MicroHudRendererModule> availableRendererModules = getAvailableRendererModules();
        MicroHudOptions.getInstance().getEnabledRendererModules().forEach(s -> {
            availableRendererModules.forEach(microHudRendererModule -> {
                if(microHudRendererModule.getClass().getSimpleName().equals(s)) register(microHudRendererModule);
            });
        });
    }

    private static MicroHudRenderer instance = null;

    public static MicroHudRenderer getInstance(){
        if(instance == null) instance = new MicroHudRenderer();
        return instance;
    }

    public static ArrayList<MicroHudRendererModule> getAvailableRendererModules(){
        ArrayList<MicroHudRendererModule> availableRendererModules = new ArrayList<>();

        availableRendererModules.add(new MHDateTimeRendererModule());
        availableRendererModules.add(new MHPlayerPositionRendererModule());
        availableRendererModules.add(new MHFpsRendererModule());
        availableRendererModules.add(new MHLookingAtRenderModule());
        availableRendererModules.add(new MHPlayerCountModule());
        availableRendererModules.add(new MHServerBrandModule());
        availableRendererModules.add(new MHCurrentBiomeModule());
        availableRendererModules.add(new MHWeatherTemperatureAndConditionModule());
        availableRendererModules.add(new MHWeatherWindAndHumidityModule());
        availableRendererModules.add(new MHFacingModule());

        return availableRendererModules;
    }

    final private TextRenderer RENDERER = MinecraftClient.getInstance().inGameHud.getTextRenderer();
    @Getter
    private final ArrayList<MicroHudRendererModule> rendererModules = new ArrayList<>();
    public boolean isModuleEnabled(Class<? extends MicroHudRendererModule> c){
        for (MicroHudRendererModule rendererModule:
             rendererModules) {
            if (rendererModule.getClass().equals(c)) return true;
        }
        return false;
    }

    public void enableRendererModule(MicroHudRendererModule module){
        MicroHudRenderer.getInstance().getRendererModules().add(module);
        module.onEnable(true);
    }

    public void disableRendererModule(MicroHudRendererModule module){
        MicroHudRenderer.getInstance().getRendererModules().remove(module);
        module.onEnable(false);
    }

    @Setter
    private boolean enabled = true;

    public void register(MicroHudRendererModule microHudRendererModule){
        rendererModules.add(microHudRendererModule);
    }

    public void render(DrawContext context, float deltaT, int scaledWidth){
        if (!enabled) return;

        int statusEffectOffset = 0;
        if (Objects.nonNull(client.player) && !client.player.getStatusEffects().isEmpty()) statusEffectOffset = (int) (RENDERER.fontHeight * 2.8);
        String text;
        for(int i = 0; i < rendererModules.size(); i++){
            text = rendererModules.get(i).render(deltaT);
            if (Objects.isNull(text)) continue;
            MHGuiUtil.drawText(context, RENDERER, text, scaledWidth - RENDERER.getWidth(text) - 5, i * RENDERER.fontHeight + 5 + statusEffectOffset, MicroHudColors.WHITE);
        }

        //flag for gcc
        text = null;
    }

    public boolean getEnabled() {return enabled;}

    public void toggleEnabled(){
        enabled = !enabled;
    }
}
