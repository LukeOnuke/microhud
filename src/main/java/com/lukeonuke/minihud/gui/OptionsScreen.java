package com.lukeonuke.minihud.gui;

import com.lukeonuke.minihud.MicroHud;
import com.lukeonuke.minihud.data.MicroHudOptions;
import com.lukeonuke.minihud.gui.list.MHLineList;
import com.lukeonuke.minihud.gui.list.MHList;
import com.lukeonuke.minihud.renderer.MicroHudRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class OptionsScreen extends Screen {
    private final int padding = 8;
    private MHList availableList;
    private MHList selectedList;
    private MHLineList selected;
    private MicroHudRenderer renderer;
    final private MicroHudOptions microHudOptions = MicroHudOptions.getInstance();

    public OptionsScreen() {
        super(Text.of("Options"));

    }

    @Override
    protected void init() {
        super.init();
        renderer = MicroHudRenderer.getInstance();

        ButtonWidget doneButton = this.addDrawableChild(new ButtonWidget(this.width - 100 - padding, this.height - 20 - padding, 100, 20, ScreenTexts.DONE, (event) -> {
            MicroHud.LOGGER.info("Done");
            exit(true);
        }));

        this.addDrawableChild(new ButtonWidget(padding, this.height - 20 - padding, 100, 20, ScreenTexts.CANCEL, (event)->{
            MicroHud.LOGGER.info("Cancled options screen");
            exit(false);
        }));



        //super(client, width, height, 32, height - 55 + 4, 36);
        /*
        * 36 - gornja grana
        *
        * */
        //MHLineList lineList = this.addDrawableChild(new MHLineList(0, padding + textRenderer.fontHeight + padding, textRenderer));
        int w = 150;
        int h = this.height - padding * 3 - textRenderer.fontHeight;

        availableList = new MHList(client, w, h, (textRenderer.fontHeight + padding) * 2 + padding, this.height - padding, textRenderer.fontHeight + padding);
        this.addSelectableChild(availableList);
        availableList.setLeftPos(padding);

        //selectedList = new MHList(client, 200, h, textRenderer.fontHeight + padding * 2, this.height - padding, textRenderer.fontHeight + padding);
        //this.addSelectableChild(selectedList);
        //selectedList.setLeftPos(this.width / 2);

        selected = this.addDrawableChild(new MHLineList(this.width / 2, (textRenderer.fontHeight + padding) * 2 + padding, textRenderer, availableList));


        MicroHud.LOGGER.info("{} {}", (this.width * 0.6D), (int)(this.width * 0.6D));


        refreshLists();

        /*this.addDrawableChild(new ButtonWidget(this.width / 2 - 10, this.height / 2 - 10, 20, 20, Text.of(">"), (event) -> {
            MHList.Entry entry = availableList.getFocused();
            MicroHud.LOGGER.info("Adding selected {}", entry);
            if(entry == null) return;
            availableList.removeEntry(entry);
            selectedList.addEntry(entry);
        }));*/
        renderer.setEnabled(false);
    }

    public void refreshLists(){
        availableList.children().clear();
        MicroHudRenderer.getAvailableRendererModules().forEach(rendererModule -> {
            AtomicBoolean contains = new AtomicBoolean(false);
            renderer.getRendererModules().forEach(enabledModules -> {
                if(enabledModules.getClass().equals(rendererModule.getClass())) contains.set(true);
            });
            if(!contains.get()) availableList.addEntry(new MHList.Entry(true, false, rendererModule, textRenderer, availableList));
        });



        //selectedList.children().clear();
        //renderer.getRendererModules().forEach(rendererModule -> {
        //    selectedList.addEntry(new MHList.Entry(false, true, rendererModule, textRenderer, selectedList, availableList));
        //});
    }

    private void exit(boolean save){
        if(save){
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
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        //this.fillGradient(matrices, 0, 0, this.width, this.height, 0x0101010F, 0x0101010F);
        DrawableHelper.fill(matrices, 0, 0, this.width, this.height, 0x0101010F);
        availableList.render(matrices, mouseX, mouseY, delta);
        //selectedList.render(matrices, mouseX, mouseY, delta);
        Text title = Text.translatable("gui.microhud.configuration.title");
        textRenderer.draw(matrices, title, (this.width - textRenderer.getWidth(title.getString())) / 2F, padding, 0xFFFFFF);

        textRenderer.draw(matrices, Text.translatable("gui.microhud.configuration.available"), padding, padding * 2 + textRenderer.fontHeight, 0xFFFFFF);
        textRenderer.draw(matrices, Text.translatable("gui.microhud.configuration.selected"), this.width / 2F, padding * 2 + textRenderer.fontHeight, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        renderer.setEnabled(true);
        super.close();
    }
}
