package com.lukeonuke.minihud.gui.list;

import com.lukeonuke.minihud.MicroHudColors;
import com.lukeonuke.minihud.gui.MHGuiUtil;
import com.lukeonuke.minihud.renderer.MicroHudRenderer;
import com.lukeonuke.minihud.renderer.module.MicroHudRendererModule;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.text.Text;

public class MHList extends AlwaysSelectedEntryListWidget<MHList.Entry> {
    public MHList(MinecraftClient client, int width, int height, int top, int itemHeight) {
        super(client, width, height, top, itemHeight);
    }

    @Override
    public int addEntry(Entry entry) {
        return super.addEntry(entry);
    }

    @Override
    public boolean removeEntry(Entry entry) {
        return super.removeEntry(entry);
    }

    @Override
    protected void drawMenuListBackground(DrawContext context) {
        // Remove background.
    }

    @Override
    protected void drawHeaderAndFooterSeparators(DrawContext context) {
        // Remove that pesky line at the top of the list widget.
    }

    public static class Entry extends AlwaysSelectedEntryListWidget.Entry<Entry> implements AutoCloseable, Element {
        private final boolean showName;
        @Getter
        private final MicroHudRendererModule rendererModule;
        private final TextRenderer textRenderer;
        private final MHList widget;

        public Entry(boolean showName, MicroHudRendererModule microHudRenderer, TextRenderer textRenderer, MHList widget) {
            this.showName = showName;
            this.rendererModule = microHudRenderer;
            this.textRenderer = textRenderer;
            this.widget = widget;
        }

        @Override
        public void close() {

        }

        @Override
        public Text getNarration() {
            return Text.translatable("narration.microhud.mhlist");
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            int color = 0xFFFFFF;
            if (hovered) {
                color = MicroHudColors.HOVER;
                // Background highlight on cover.
                //context.fill(x, y, x + entryWidth, y + entryHeight, -1, MicroHudColors.TRANSLUCENT);
            }

            String text = rendererModule.getName();
            if (!showName) text = rendererModule.render(tickDelta);
            MHGuiUtil.drawText(context, textRenderer, text, (int) (entryWidth * 0.25 + x), y, color);
        }


        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            MicroHudRenderer.getInstance().enableRendererModule(rendererModule);
            widget.removeEntry(this);
            return false;
        }

        @Override
        public String toString() {
            return getClass().getName() +
                    " - " +
                    rendererModule.getClass().getName();
        }
    }
}
