package com.lukeonuke.minihud;

import com.lukeonuke.minihud.data.MicroHudOptions;
import com.lukeonuke.minihud.gui.OptionsScreen;
import com.lukeonuke.minihud.renderer.MicroHudRenderer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MicroHud implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("microhud");

    private static final KeyBinding.Category MICROHUD_CATEGORY = KeyBinding.Category.create(
            Identifier.of("microhud", "main") // Use Identifier.of() instead of 'new Identifier()'
    );

    public static KeyBinding toggleOptionsScreen;
    public static KeyBinding toggleRenderer;

    public static final int PADDING = 5;

    @Override
    public void onInitialize() {
        MicroHudOptions.getInstance().refresh();

        toggleOptionsScreen = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.microhud.toggleOptionsScreen",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_U,
                MICROHUD_CATEGORY // Pass the Category object here
        ));

        toggleRenderer = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.microhud.toggleRenderer", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The keycode of the key
                GLFW.GLFW_KEY_F6, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                MICROHUD_CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (toggleOptionsScreen.wasPressed()) client.setScreen(new OptionsScreen());
            if (toggleRenderer.wasPressed()) MicroHudRenderer.getInstance().toggleEnabled();
        });

        LOGGER.info("Microhud initialised!");
    }
}
