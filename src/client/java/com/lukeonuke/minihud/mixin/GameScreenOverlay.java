package com.lukeonuke.minihud.mixin;

import com.lukeonuke.minihud.renderer.MicroHudRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class GameScreenOverlay {
    @Final
    @Shadow
    private MinecraftClient client;

    @Shadow
    @Final
    private DebugHud debugHud;

    @Inject(at = @At("TAIL"), method = "render")
    void renderMicroHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (!(debugHud.shouldShowDebugHud() || this.client.options.hudHidden))
            MicroHudRenderer.getInstance().render(context, tickCounter.getTickDelta(false), context.getScaledWindowWidth());
    }

}
