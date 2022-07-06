package com.lukeonuke.minihud.renderer;

import com.lukeonuke.minihud.renderer.module.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.Objects;

public class MicroHudRenderer {

    private MicroHudRenderer() {
        this.register(new MHDDateTimeRendererModule());
        this.register(new MHDPlayerPositionRendererModule());
        this.register(new MHDLookingAtRenderModule());
        this.register(new MHDFpsRendererModule());
    }

    private static MicroHudRenderer instance = null;

    public static MicroHudRenderer getInstance(){
        if(instance == null) instance = new MicroHudRenderer();
        return instance;
    }

    final private TextRenderer RENDERER = MinecraftClient.getInstance().inGameHud.getTextRenderer();
    private final ArrayList<MicroHudRendererModule> rendererModules = new ArrayList<>();

    public void register(MicroHudRendererModule microHudRendererModule){
        rendererModules.add(microHudRendererModule);
    }

    public void render(MatrixStack matrixStack, float deltaT, int scaledWidth){
        String text;
        for(int i = 0; i < rendererModules.size(); i++){
            text = rendererModules.get(i).render(deltaT);
            if (Objects.isNull(text)) continue;
            RENDERER.drawWithShadow(matrixStack, text, scaledWidth - RENDERER.getWidth(text), i * RENDERER.fontHeight + 5, 0xfcfcfa);
        }

        //flag for gcc
        text = null;
    }
}
