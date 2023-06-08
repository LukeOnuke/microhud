package com.lukeonuke.minihud.gui;

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
}
