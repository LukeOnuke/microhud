package com.lukeonuke.minihud.gui;

import com.lukeonuke.minihud.MicroHud;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
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

    public void render(DrawContext context, TextRenderer textRenderer, int x, int y){
        this.x = x;
        this.y = y;
        width = textRenderer.fontHeight + padding * 2;
        height = textRenderer.getWidth(content) + padding * 2;

        context.fill(x, y, x + width, y + width, 0x000000FA);
        MHGuiUtil.drawText(context, textRenderer, content, x + padding, y + padding, 0xFFFFFF, false);
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
