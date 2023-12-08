package com.lukeonuke.minihud;

import com.lukeonuke.minihud.gui.OptionsScreen;
import com.lukeonuke.minihud.data.MicroHudOptions;
import com.lukeonuke.minihud.renderer.MicroHudRenderer;
import com.lukeonuke.minihud.service.WeatherService;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

public class MicroHud implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("microhud");

    public static KeyBinding toggleOptionsScreen;
    public static KeyBinding toggleRenderer;

    public static final int PADDING = 5;
    @Override
    public void onInitialize() {
        toggleOptionsScreen = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.microhud.toggleOptionsScreen", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_U, // The keycode of the key
                "category.microhud.controls" // The translation key of the keybinding's category.
        ));

        toggleRenderer = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.microhud.toggleRenderer", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_F6, // The keycode of the key
                "category.microhud.controls" // The translation key of the keybinding's category.
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (toggleOptionsScreen.wasPressed()) client.setScreen(new OptionsScreen());
            if (toggleRenderer.wasPressed()) MicroHudRenderer.getInstance().toggleEnabled();
        });

        MicroHudOptions.getInstance().refresh();

        LOGGER.info("Microhud initialised");
    }
}
