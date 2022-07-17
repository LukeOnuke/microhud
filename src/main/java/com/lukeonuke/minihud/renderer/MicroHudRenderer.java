package com.lukeonuke.minihud.renderer;

import com.lukeonuke.minihud.data.MicroHudOptions;
import com.lukeonuke.minihud.renderer.module.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.Objects;

public class MicroHudRenderer {

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

    public ArrayList<MicroHudRendererModule> getRendererModules() {
        return rendererModules;
    }

    public static ArrayList<MicroHudRendererModule> getAvailableRendererModules(){
        ArrayList<MicroHudRendererModule> availableRendererModules = new ArrayList<>();

        availableRendererModules.add(new MHDateTimeRendererModule());
        availableRendererModules.add(new MHPlayerPositionRendererModule());
        availableRendererModules.add(new MHFpsRendererModule());
        availableRendererModules.add(new MHLookingAtRenderModule());
        availableRendererModules.add(new MHPlayerCountModule());
        availableRendererModules.add(new MHServerBrandModule());

        return availableRendererModules;
    }

    final private TextRenderer RENDERER = MinecraftClient.getInstance().inGameHud.getTextRenderer();
    private final ArrayList<MicroHudRendererModule> rendererModules = new ArrayList<>();
    private boolean enabled = true;

    public void register(MicroHudRendererModule microHudRendererModule){
        rendererModules.add(microHudRendererModule);
    }

    public void render(MatrixStack matrixStack, float deltaT, int scaledWidth){
        if (!enabled) return;
        String text;
        for(int i = 0; i < rendererModules.size(); i++){
            text = rendererModules.get(i).render(deltaT);
            if (Objects.isNull(text)) continue;
            RENDERER.drawWithShadow(matrixStack, text, scaledWidth - RENDERER.getWidth(text), i * RENDERER.fontHeight + 5, 0xFFFFFF);
        }

        //flag for gcc
        text = null;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void toggleEnabled(){
        enabled = !enabled;
    }
}
