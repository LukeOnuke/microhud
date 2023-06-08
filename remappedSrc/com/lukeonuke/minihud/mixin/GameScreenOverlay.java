package com.lukeonuke.minihud.mixin;

import com.lukeonuke.minihud.MicroHud;
import com.lukeonuke.minihud.gui.OptionsScreen;
import com.lukeonuke.minihud.renderer.MicroHudRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class GameScreenOverlay {
    @Shadow private int scaledWidth;



    @Inject(at = @At("HEAD"), method = "render")
    void onHudRender(MatrixStack matrixStack, float tickDelta, CallbackInfo ci){
        if (!MinecraftClient.getInstance().options.debugEnabled) MicroHudRenderer.getInstance().render(matrixStack, tickDelta, scaledWidth);

    }

}
