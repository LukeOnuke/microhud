package com.lukeonuke.minihud.gui.list;

import com.lukeonuke.minihud.MicroHud;
import com.lukeonuke.minihud.MicroHudColors;
import com.lukeonuke.minihud.gui.MHGuiUtil;
import com.lukeonuke.minihud.renderer.MicroHudRenderer;
import com.lukeonuke.minihud.renderer.module.MicroHudRendererModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MHLineList implements Drawable, Selectable, Element {
    protected final int x;
    protected final int y;
    private final int padding = 5;
    private final MicroHudRenderer microHudRenderer = MicroHudRenderer.getInstance();
    private final MinecraftClient client = MinecraftClient.getInstance();
    protected final TextRenderer textRenderer;
    private final MHList other;

    private int color = MicroHudColors.WHITE;

    public MHLineList(int x, int y, TextRenderer textRenderer, MHList other) {
        this.x = x;
        this.y = y;
        this.textRenderer = textRenderer;
        this.other = other;
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if(Objects.isNull(client.currentScreen)) return;
        ArrayList<MicroHudRendererModule> modules = microHudRenderer.getRendererModules();
        int yPos;
        int xPos = (int)(client.currentScreen.width / 1.75);

        for (int i = 0; i < modules.size(); i++) {
            color = MicroHudColors.WHITE;
            yPos = padding * (i + 1) + textRenderer.fontHeight * i;
            yPos += y;
            renderButtons(context, yPos);
            if(MHGuiUtil.isHovered(xPos, yPos, client.currentScreen.width / 2, textRenderer.fontHeight, mouseX, mouseY))
                color = MicroHudColors.HOVER;

            //right side
            MHGuiUtil.drawText(context, textRenderer, modules.get(i).render(delta), xPos, yPos, color, true);
            //textRenderer.draw(modules.get(i).render(delta), xPos, yPos, color, true, context.getMatrices().peek().getPositionMatrix(), context.getVertexConsumers(), TextRenderer.TextLayerType.SEE_THROUGH, MicroHudColors.TRANSPARENT, MicroHudColors.LIGHT);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(Objects.isNull(client.currentScreen)) return false;

        if(mouseX < client.currentScreen.width / 1.75) return false;
        int top;
        for (int i = 0; i < microHudRenderer.getRendererModules().size(); i++) {
            top = padding + (padding + textRenderer.fontHeight) * i + y;
            if (mouseY > top && mouseY < top + textRenderer.fontHeight) {
                int rightEdge = client.currentScreen.width - padding;
                if(mouseX < rightEdge && mouseX > rightEdge - textRenderer.getWidth("U")){
                    // up
                    switchRenderers(i, i - 1);
                }else {
                    rightEdge -= padding + textRenderer.getWidth("U");
                    if(mouseX < rightEdge && mouseX > rightEdge - textRenderer.getWidth("D")) {
                        // down
                        switchRenderers(i, i + 1);
                    }else{
                        other.addEntry(new MHList.Entry(true, false, microHudRenderer.getRendererModules().get(i), textRenderer, other));
                        microHudRenderer.getRendererModules().remove(microHudRenderer.getRendererModules().get(i));
                    }
                }
            }
        }
        //}

        return Element.super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void setFocused(boolean focused) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public SelectionType getType() {
        return SelectionType.NONE;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    private void renderButtons(DrawContext context, int yPos) {
        if (client.currentScreen == null) return;

        MHGuiUtil.drawText(context, textRenderer, "U", client.currentScreen.width - padding - textRenderer.getWidth("U"), yPos, MicroHudColors.GREEN);
        MHGuiUtil.drawText(context, textRenderer, "D", client.currentScreen.width - (padding + textRenderer.getWidth("U")) * 2, yPos, MicroHudColors.RED);
    }

    private void switchRenderers(int currentPosition, int desiredPosition){
        ArrayList<MicroHudRendererModule> rendererModules = microHudRenderer.getRendererModules();
        if(!belongsToList(rendererModules, currentPosition)) return;
        if(!belongsToList(rendererModules, desiredPosition)) return;
        MicroHudRendererModule mem = rendererModules.get(desiredPosition);
        rendererModules.set(desiredPosition, rendererModules.get(currentPosition));
        rendererModules.set(currentPosition, mem);
    }

    private boolean belongsToList(List<?> list, int index){
        return index < list.size() && index >= 0;
    }
}
