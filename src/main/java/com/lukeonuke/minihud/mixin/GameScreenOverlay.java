package com.lukeonuke.minihud.mixin;

import com.lukeonuke.minihud.MicroHud;
import com.lukeonuke.minihud.gui.OptionsScreen;
import com.lukeonuke.minihud.renderer.MicroHudRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class GameScreenOverlay{
    @Shadow private int scaledWidth;
    @Shadow private MinecraftClient client;

    @Shadow @Final private DebugHud debugHud;

    @Inject(at = @At("TAIL"), method = "render")
    void renderMicroHud(DrawContext context, float tickDelta, CallbackInfo ci){
        if (!(debugHud.shouldShowDebugHud() || this.client.options.hudHidden))
            MicroHudRenderer.getInstance().render(context, tickDelta, scaledWidth);
    }

}
