package com.lukeonuke.minihud.gui.list;

import com.lukeonuke.minihud.MicroHud;
import com.lukeonuke.minihud.MicroHudColors;
import com.lukeonuke.minihud.renderer.MicroHudRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.util.math.MatrixStack;

public class ScrollingText implements Drawable {
    private int bottom;
    private TextRenderer textRenderer;
    private final String text;

    private int currentBeginning = 0;
    private int width;
    private int textWidth;
    private float time;

    private String currentText;

    private MinecraftClient client = MinecraftClient.getInstance();
    private final int padding = MicroHud.PADDING;

    public ScrollingText(int bottom, TextRenderer textRenderer, String text) {
        this.bottom = bottom;
        this.textRenderer = textRenderer;
        this.text = text;
        textWidth = textRenderer.getWidth(text);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (client.currentScreen == null) return;

        currentText = text;
        width = client.currentScreen.width;
        if(width < textWidth){
            time += delta;
            if(time > 10) {
                time = 0;
                currentBeginning++;
                if(currentBeginning >= text.length()) currentBeginning = 0;
            }
            currentText = text.substring(currentBeginning);
        }else currentBeginning = 0;
        textRenderer.draw(matrices, currentText, padding, client.currentScreen.height - bottom, MicroHudColors.WHITE);
    }
}
