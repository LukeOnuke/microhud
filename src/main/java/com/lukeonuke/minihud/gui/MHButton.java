package com.lukeonuke.minihud.gui;

import com.lukeonuke.minihud.MicroHud;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class MHButton {
    Runnable onClick;
    int x;
    int y;
    int width;
    int height;
    int padding = MicroHud.PADDING;
    Text content;

    public MHButton(Text content, Runnable onClick) {
        this.onClick = onClick;
        this.content = content;
    }

    public void render(MatrixStack matrices, TextRenderer textRenderer, int x, int y){
        this.x = x;
        this.y = y;
        width = textRenderer.fontHeight + padding * 2;
        height = textRenderer.getWidth(content) + padding * 2;

        DrawableHelper.fill(matrices, x, y, x + width, y + width, 0x000000FA);
        textRenderer.draw(matrices, content, x + padding, y + padding, 0xFFFFFF);
    }

    public void click(double cx, double cy){
        if(cx > x && cx < x + width){
            if(cy > y && cy < y + height){
                onClick.run();
            }
        }
    }

    public Text getContent() {
        return content;
    }
}
