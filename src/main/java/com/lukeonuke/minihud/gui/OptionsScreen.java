package com.lukeonuke.minihud.gui;

import com.lukeonuke.minihud.MicroHud;
import com.lukeonuke.minihud.MicroHudColors;
import com.lukeonuke.minihud.data.MicroHudOptions;
import com.lukeonuke.minihud.gui.list.MHLineList;
import com.lukeonuke.minihud.gui.list.MHList;
import com.lukeonuke.minihud.gui.list.ScrollingText;
import com.lukeonuke.minihud.renderer.MicroHudRenderer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class OptionsScreen extends Screen {
    private final int padding = 8;
    private MHList availableList;
    private MHList selectedList;
    private MHLineList selected;
    private MicroHudRenderer renderer;
    final private MicroHudOptions microHudOptions = MicroHudOptions.getInstance();

    private boolean wasLineRendererEnabled;

    public OptionsScreen() {
        super(Text.of("Options"));

    }

    @Override
    protected void init() {
        super.init();
        renderer = MicroHudRenderer.getInstance();

        ButtonWidget doneButton = this.addDrawableChild(new ButtonWidget.Builder(ScreenTexts.DONE, (event) -> {
            MicroHud.LOGGER.info("Done");
            exit(true);
        }).dimensions(this.width - 100 - padding, this.height - 20 - padding, 100, 20).build());

        //padding, this.height - 20 - padding, 100, 20, ScreenTexts.CANCEL
        this.addDrawableChild(new ButtonWidget.Builder(ScreenTexts.CANCEL, (event) -> {
            MicroHud.LOGGER.info("Cancled options screen");
            exit(false);
        }).dimensions(padding, this.height - 20 - padding, 100, 20).build());

        ButtonWidget playerDiscordToggleButtonWidget = this.addDrawableChild(new ButtonWidget.Builder(Text.empty(), (event) -> {
            microHudOptions.setEnabledPlayerDiscordTag(!microHudOptions.getEnabledPlayerDiscordTag());
            updatePlayerDiscordTagToggleButton(event);
        }).dimensions(this.width / 2 - 75, this.height - 20 - padding, 150, 20).build());
        updatePlayerDiscordTagToggleButton(playerDiscordToggleButtonWidget);

        ButtonWidget privacyPolicyButton = this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("gui.microhud.configuration.privacyPolicy"), (event) -> {
            try {
                Util.getOperatingSystem().open(new URI("https://weather.lukeonuke.com/privacy/privacypolicy/"));
            } catch (URISyntaxException e) {
                MicroHud.LOGGER.error("Couldn't navigate to privacy policy: " + e.getMessage());
            }
            exit(false);
        }).dimensions(this.width - padding*2 - textRenderer.getWidth(Text.translatable("gui.microhud.configuration.privacyPolicy")), 2, textRenderer.getWidth(Text.translatable("gui.microhud.configuration.privacyPolicy")) + padding, 20).build());

        this.addDrawable(
                new ScrollingText(
                        20 + textRenderer.fontHeight + padding * 2, textRenderer,
                        Text.translatable("gui.microhud.configuration.tip").getString()
                )
        );

        //super(client, width, height, 32, height - 55 + 4, 36);
        /*
         * 36 - gornja grana
         *
         * */
        //MHLineList lineList = this.addDrawableChild(new MHLineList(0, padding + textRenderer.fontHeight + padding, textRenderer));
        int w = 150;
        int h = this.height - padding * 3 - textRenderer.fontHeight;

        availableList = new MHList(client, w, h, (textRenderer.fontHeight + padding) * 2 + padding * 2, this.height - (20 + textRenderer.fontHeight + padding * 3), textRenderer.fontHeight + padding);
        this.addSelectableChild(availableList);
        availableList.setLeftPos(padding);

        //selectedList = new MHList(client, 200, h, textRenderer.fontHeight + padding * 2, this.height - padding, textRenderer.fontHeight + padding);
        //this.addSelectableChild(selectedList);
        //selectedList.setLeftPos(this.width / 2);

        selected = this.addDrawableChild(new MHLineList(this.width / 2, (textRenderer.fontHeight + padding) * 2 + padding * 2, textRenderer, availableList));

        refreshLists();

        /*this.addDrawableChild(new ButtonWidget(this.width / 2 - 10, this.height / 2 - 10, 20, 20, Text.of(">"), (event) -> {
            MHList.Entry entry = availableList.getFocused();
            MicroHud.LOGGER.info("Adding selected {}", entry);
            if(entry == null) return;
            availableList.removeEntry(entry);
            selectedList.addEntry(entry);
        }));*/
        wasLineRendererEnabled = renderer.getEnabled();
        renderer.setEnabled(false);
    }

    public void refreshLists() {
        availableList.children().clear();
        MicroHudRenderer.getAvailableRendererModules().forEach(rendererModule -> {
            AtomicBoolean contains = new AtomicBoolean(false);
            renderer.getRendererModules().forEach(enabledModules -> {
                if (enabledModules.getClass().equals(rendererModule.getClass())) contains.set(true);
            });
            if (!contains.get())
                availableList.addEntry(new MHList.Entry(true, false, rendererModule, textRenderer, availableList));
        });


        //selectedList.children().clear();
        //renderer.getRendererModules().forEach(rendererModule -> {
        //    selectedList.addEntry(new MHList.Entry(false, true, rendererModule, textRenderer, selectedList, availableList));
        //});
    }

    private void exit(boolean save) {
        if (save) {
            ArrayList<String> classes = new ArrayList<>();
            renderer.getRendererModules().forEach(rendererModule -> {
                classes.add(rendererModule.getClass().getSimpleName());
            });
            microHudOptions.setEnabledRendererModules(classes);
            microHudOptions.save();
        }
        this.close();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //this.fillGradient(matrices, 0, 0, this.width, this.height, 0x0101010F, 0x0101010F);
        final MatrixStack matrices = context.getMatrices();
        final VertexConsumerProvider vertexConsumerProvider = context.getVertexConsumers();
        context.fill(0, 0, this.width, this.height, MicroHudColors.TRANSLUCENT);
        context.fill(0, 0, this.width, padding * 2 + textRenderer.fontHeight, MicroHudColors.TRANSLUCENT);
        availableList.render(context, mouseX, mouseY, delta);
        //selectedList.render(matrices, mouseX, mouseY, delta);
        Text title = Text.translatable("gui.microhud.configuration.title");

        //textRenderer.draw(title, (this.width - textRenderer.getWidth(title.getString())) / 2F, padding, 0xFFFFFF, false, matrices.peek().getPositionMatrix(), vertexConsumerProvider, TextRenderer.TextLayerType.SEE_THROUGH, 0x000000, 16);

        //textRenderer.draw(Text.translatable("gui.microhud.configuration.available"), padding, padding * 3 + textRenderer.fontHeight, 0xFFFFFF, false, matrices.peek().getPositionMatrix(), vertexConsumerProvider, TextRenderer.TextLayerType.SEE_THROUGH, 0x000000, 16);
        //textRenderer.draw(Text.translatable("gui.microhud.configuration.selected"), this.width / 2F, padding * 3 + textRenderer.fontHeight, 0xFFFFFF, false, matrices.peek().getPositionMatrix(), vertexConsumerProvider, TextRenderer.TextLayerType.SEE_THROUGH, 0x000000, 16);

        MHGuiUtil.drawText(context, textRenderer, title, ((this.width - textRenderer.getWidth(title.getString())) / 2), padding, 0xFFFFFF, false);

        MHGuiUtil.drawText(context, textRenderer, Text.translatable("gui.microhud.configuration.available"), padding, padding * 3 + textRenderer.fontHeight, 0xFFFFFF, false);
        MHGuiUtil.drawText(context, textRenderer, Text.translatable("gui.microhud.configuration.selected"), this.width / 2, padding * 3 + textRenderer.fontHeight, 0xFFFFFF, false);

        if (!wasLineRendererEnabled) MHGuiUtil.drawText(context, textRenderer, MutableText.of(new TranslatableTextContent("gui.microhud.configuration.renderDisabled", null, new Object[]{MicroHud.toggleRenderer.getBoundKeyLocalizedText().getString()})).getString(), padding, padding * 3, MicroHudColors.RED);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        renderer.setEnabled(wasLineRendererEnabled);
        super.close();
    }

    private void updatePlayerDiscordTagToggleButton(ButtonWidget buttonWidget){
        if(microHudOptions.getEnabledPlayerDiscordTag()){
            buttonWidget.setMessage(Text.of("Player discord tag: " + Formatting.BOLD + Formatting.GREEN + "ON"));
        }else{
            buttonWidget.setMessage(Text.of("Player discord tag: " + Formatting.BOLD + Formatting.RED + "OFF"));
        }
    }
}
