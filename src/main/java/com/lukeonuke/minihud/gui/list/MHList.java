package com.lukeonuke.minihud.gui.list;

import com.lukeonuke.minihud.MicroHud;
import com.lukeonuke.minihud.MicroHudColors;
import com.lukeonuke.minihud.data.MicroHudOptions;
import com.lukeonuke.minihud.renderer.MicroHudRenderer;
import com.lukeonuke.minihud.renderer.module.MicroHudRendererModule;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

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

        private boolean isMouseClicked;
        private double mX, mY;

        private MHList otherList;
        private int padding = MicroHud.PADDING;

        private float tickD = 0F;
        boolean toggleDown = true; //its bad programming, but it works, unlike THE INTENDED WAY aah

        public Entry(boolean showName, boolean movableControls, MicroHudRendererModule microHudRenderer, TextRenderer textRenderer, MHList widget) {
            this.showName = showName;
            this.movableControls = movableControls;
            this.rendererModule = microHudRenderer;
            this.textRenderer = textRenderer;
            this.widget = widget;

            ScreenMouseEvents.afterMouseClick(client.currentScreen).register((screen, mouseX, mouseY, button) -> {
                if (button == 0) isMouseClicked = true;
                mX = mouseX;
                mY = mouseY;
            });
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
            tickD += tickDelta;
            int u = textRenderer.getWidth("U");

            DrawableHelper.fill(matrices, x, y, x + entryWidth, x + entryHeight, MicroHudColors.BLUE);
            hovered = isHovered(x, y, entryWidth, entryHeight, mouseX, mouseY);

            if (movableControls) {
                textRenderer.draw(matrices, "U", x + padding * 2 + padding, y, MicroHudColors.GREEN);
                textRenderer.draw(matrices, "D", x + padding * 2 + u * 2 + padding * 2, y, MicroHudColors.GREEN);
            }

            int color = 0xFFFFFF;
            if (hovered) {

                color = MicroHudColors.HOVER;

                if (isMouseClicked) {
                    int cx = (int)mouseX - widget.getRowLeft() - padding * 2;

                    if (movableControls) {
                        isMouseClicked = false;
                        if (cx > padding && cx < padding + u) {
                            switchLists(index, index - 1);
                        }
                        if (cx > (padding + u) * 2 && cx < (padding + u) * 2 + u) {
                            //if(downCounter >= 2) {
                            if(toggleDown){
                                switchLists(index, index + 1);
                                tickD = 0F;
                            }
                            MicroHud.LOGGER.info("{}", toggleDown);
                            toggleDown = !toggleDown;

                            //    downCounter = 0;
                            //} else {
                            //    downCounter++;
                            //}
                        }
                    }
                }
            }


            if (!Objects.isNull(widget.getSelectedOrNull())) {
                if (widget.getSelectedOrNull().equals(this)) color = MicroHudColors.GREEN;
            }

            String text = rendererModule.getName();
            if (!showName) text = rendererModule.render(tickDelta);
            textRenderer.draw(matrices, text, (int) (entryWidth * 0.25 + x), y, color);
            isMouseClicked = false;
        }


        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            MicroHud.LOGGER.info("Click {}", this);
            /*boolean clicked = false;

            double top = this.widget.getRowTop(widget.children().indexOf(this));
            double left = this.widget.getRowLeft();
            if(mouseX > left & mouseX < left + widget.width){
                if(mouseY > top && mouseY < top + widget.itemHeight){
                    clicked = true;
                    MicroHud.LOGGER.info("Clicked on {}", this);
                }
            }*/
            /*int u = textRenderer.getWidth("U");
            int cx = (int)mouseX - widget.getRowLeft() - padding * 2;
            int index = widget.children().indexOf(this);
            if (movableControls) {
                if (cx > padding && cx < padding + u) {
                    MicroHud.LOGGER.info("Moving up {}", this);
                    switchLists(index, index - 1);
                } else if (cx > (padding + u) * 2 && cx < (padding + u) * 2 + u) {
                    MicroHud.LOGGER.info("Moving down {}", this);
                    switchLists(index, index + 1);
                    MicroHud.LOGGER.info("Moving down {}", widget.children());
                }
            }*/
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
