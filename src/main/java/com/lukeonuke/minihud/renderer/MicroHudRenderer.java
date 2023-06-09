package com.lukeonuke.minihud.renderer;

import com.lukeonuke.minihud.MicroHudColors;
import com.lukeonuke.minihud.data.MicroHudOptions;
import com.lukeonuke.minihud.gui.MHGuiUtil;
import com.lukeonuke.minihud.renderer.module.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

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
        availableRendererModules.add(new MHCurrentBiomeModule());

        return availableRendererModules;
    }

    final private TextRenderer RENDERER = MinecraftClient.getInstance().inGameHud.getTextRenderer();
    private final ArrayList<MicroHudRendererModule> rendererModules = new ArrayList<>();
    private boolean enabled = true;

    public void register(MicroHudRendererModule microHudRendererModule){
        rendererModules.add(microHudRendererModule);
    }

    public void render(DrawContext context, float deltaT, int scaledWidth){
        if (!enabled) return;

        int statusEffectOffset = 0;
        if (Objects.nonNull(client.player) && client.player.getStatusEffects().size() > 0) statusEffectOffset = (int) (RENDERER.fontHeight * 2.8);
        String text;
        for(int i = 0; i < rendererModules.size(); i++){
            text = rendererModules.get(i).render(deltaT);
            if (Objects.isNull(text)) continue;
            MHGuiUtil.drawText(context, RENDERER, text, scaledWidth - RENDERER.getWidth(text) - 5, i * RENDERER.fontHeight + 5 + statusEffectOffset, MicroHudColors.WHITE);
            //RENDERER.drawWithOutline(Text.of(text).asOrderedText(), scaledWidth - RENDERER.getWidth(text) - 5, i * RENDERER.fontHeight + 5 + statusEffectOffset, 0xFFFFFFFF, 0xFFFFFFFF, context.getMatrices().peek().getPositionMatrix(), context.getVertexConsumers(), 16);
        }

        //flag for gcc
        text = null;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public boolean getEnabled() {return enabled;}

    public void toggleEnabled(){
        enabled = !enabled;
    }
}
