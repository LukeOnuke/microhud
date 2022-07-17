package com.lukeonuke.minihud.gui.list;

import com.lukeonuke.minihud.MicroHud;
import com.lukeonuke.minihud.MicroHudColors;
import com.lukeonuke.minihud.renderer.MicroHudRenderer;
import com.lukeonuke.minihud.renderer.module.MicroHudRendererModule;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class MHList extends AlwaysSelectedEntryListWidget<MHList.Entry> {
    public MHList(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
        super(client, width, height, top, bottom, itemHeight);
        this.setRenderBackground(false);
        this.setRenderHeader(false, 0);
        this.setRenderHorizontalShadows(false);
        this.setRenderSelection(true);
    }

    @Override
    public int addEntry(Entry entry) {
        return super.addEntry(entry);
    }

    @Override
    public boolean removeEntry(Entry entry) {
        return super.removeEntry(entry);
    }

    public void removeEntryByRenderer(MicroHudRendererModule rendererModule){
        for (Entry entry : children()) {
            if (entry.getRendererModule().equals(rendererModule)) removeEntry(entry);
        }
    }

    public void addIfNotPresent(Entry entry){
        if (children().contains(entry)) return;
        addEntry(entry);
    }

    public static class Entry extends AlwaysSelectedEntryListWidget.Entry<Entry> implements AutoCloseable, Element {
        private final boolean showName;
        private final boolean movableControls;
        private final MicroHudRendererModule rendererModule;
        private final TextRenderer textRenderer;
        private final MHList widget;
        private final MinecraftClient client = MinecraftClient.getInstance();

        public Entry(boolean showName, boolean movableControls, MicroHudRendererModule microHudRenderer, TextRenderer textRenderer, MHList widget) {
            this.showName = showName;
            this.movableControls = movableControls;
            this.rendererModule = microHudRenderer;
            this.textRenderer = textRenderer;
            this.widget = widget;
        }

        public MicroHudRendererModule getRendererModule() {
            return rendererModule;
        }

        @Override
        public void close() throws Exception {

        }

        @Override
        public Text getNarration() {
            return Text.translatable("narration.microhud.mhlist");
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            DrawableHelper.fill(matrices, x, y, x + entryWidth, x + entryHeight, MicroHudColors.BLUE);

            int color = 0xFFFFFF;
            if (hovered) {
                color = MicroHudColors.HOVER;
            }


            String text = rendererModule.getName();
            if (!showName) text = rendererModule.render(tickDelta);
            textRenderer.draw(matrices, text, (int) (entryWidth * 0.25 + x), y, color);
        }


        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            MicroHud.LOGGER.info("Click {}", this);
            MicroHudRenderer.getInstance().getRendererModules().add(rendererModule);
            widget.removeEntry(this);
            return false;
        }

        @Override
        public String toString() {
            return getClass().getName() +
                    " - " +
                    rendererModule.getClass().getName();
        }

        private boolean isHovered(int x, int y, int entryWidth, int entryHeight, int mouseX, int mouseY) {
            boolean hovered = false;
            if (mouseX > x && mouseX < x + entryWidth) {
                if (mouseY > y && mouseY < y + entryHeight) {
                    hovered = true;
                }
            }

            return hovered;
        }

        private void switchLists(int index, int desiredPos) {
            if (desiredPos < 0 || desiredPos > widget.children().size() - 1) return;
            MHList.Entry mem = widget.children().get(index);
            widget.children().set(index, widget.children().get(desiredPos));
            widget.children().set(desiredPos, mem);
        }
    }
}
