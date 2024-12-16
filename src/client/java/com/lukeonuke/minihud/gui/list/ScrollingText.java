package com.lukeonuke.minihud.gui.list;

import com.lukeonuke.minihud.MicroHud;
import com.lukeonuke.minihud.MicroHudColors;
import com.lukeonuke.minihud.gui.MHGuiUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;

public class ScrollingText implements Drawable {
    private final int bottom;
    private final TextRenderer textRenderer;
    private final String text;

    private int currentBeginning = 0;
    private final int textWidth;
    private float time;

    private final MinecraftClient client = MinecraftClient.getInstance();

    public ScrollingText(int bottom, TextRenderer textRenderer, String text) {
        this.bottom = bottom;
        this.textRenderer = textRenderer;
        this.text = text;
        textWidth = textRenderer.getWidth(text);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (client.currentScreen == null) return;

        String currentText = text;
        int width = client.currentScreen.width;
        if (width < textWidth) {
            time += delta;
            if (time > 15) {
                time = 0;
                currentBeginning++;
                if (currentBeginning >= text.length()) currentBeginning = 0;
            }
            currentText = text.substring(currentBeginning);
        } else currentBeginning = 0;
        MHGuiUtil.drawText(context, textRenderer, currentText, MicroHud.PADDING, client.currentScreen.height - bottom, MicroHudColors.WHITE);
    }
}
