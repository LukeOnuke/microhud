package com.lukeonuke.minihud.renderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class MicroHudRenderer {
    private MicroHudRenderer() {
    }

    private static MicroHudRenderer instance = null;
    public static MicroHudRenderer getInstance(){
        if(instance == null) instance = new MicroHudRenderer();
        return instance;
    }

    final private TextRenderer RENDERER = MinecraftClient.getInstance().inGameHud.getTextRenderer();

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");
    LocalDateTime date;

    public void render(MatrixStack matrixStack, float deltaT, int scaledWidth){
        date = LocalDateTime.now();

        layoutAndRender(
                List.of(dateTimeFormatter.format(date),
                        MinecraftClient.getInstance().player != null ? MinecraftClient.getInstance().player.getBlockPos().toShortString() : ""),
                matrixStack,
                scaledWidth);
    }

    private void layoutAndRender(List<String> text, MatrixStack matrixStack, int scaledWidth){
        for(int i = 0; i < text.size(); i++){
            if(text.get(i).equals("")) continue;
            RENDERER.drawWithShadow(matrixStack, text.get(i), scaledWidth - RENDERER.getWidth(text.get(i)), i * RENDERER.fontHeight + 5, 0xfcfcfa);
        }
    }
}
