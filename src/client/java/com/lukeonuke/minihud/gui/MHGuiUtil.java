package com.lukeonuke.minihud.gui;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class MHGuiUtil {
    public static boolean isHovered(int x, int y, int width, int height, int mouseX, int mouseY) {
        boolean hovered = false;
        if (mouseX > x && mouseX < x + width) {
            if (mouseY > y && mouseY < y + height) {
                hovered = true;
            }
        }

        return hovered;
    }

    public static void drawText(DrawContext context, TextRenderer textRenderer, Text text, int xPos, int yPos, int color, boolean shadow) {
        context.drawText(textRenderer, text, xPos, yPos, color, shadow);
    }

    public static void drawText(DrawContext context, TextRenderer textRenderer, String text, int xPos, int yPos, int color, boolean shadow) {
        drawText(context, textRenderer, Text.of(text), xPos, yPos, color, shadow);
    }

    public static void drawText(DrawContext context, TextRenderer textRenderer, String text, int xPos, int yPos, int color) {
        drawText(context, textRenderer, text, xPos, yPos, color, true);
    }
}
