package com.lukeonuke.minihud.mixin;

import com.lukeonuke.minihud.data.MicroHudOptions;
import com.lukeonuke.minihud.service.NameLookupData;
import com.lukeonuke.minihud.service.NameLookupService;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(net.minecraft.client.render.entity.EntityRenderer.class)
public abstract class EntityRenderer<T extends net.minecraft.entity.Entity, S extends net.minecraft.client.render.entity.state.EntityRenderState> {
    @Shadow
    public abstract TextRenderer getTextRenderer();

    @Unique
    private final MicroHudOptions options = MicroHudOptions.getInstance();

    @Inject(at = @At("HEAD"), method = "renderLabelIfPresent")
    protected void renderLabelIfPresent(S state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraRenderState, CallbackInfo ci) {
        if (state instanceof PlayerEntityRenderState) {
            if (options.getEnabledPlayerDiscordTag()) {
                TextRenderer renderer = getTextRenderer();
                String renderedText = Formatting.RED + "none";

                NameLookupData data = NameLookupService.getInstance().getPlayerData(((PlayerEntityRenderState) state).displayName.getString());
                if (Objects.nonNull(data)) {
                    renderedText = Formatting.DARK_GREEN + data.getTag();
                }

                queue.submitLabel(matrices, state.nameLabelPos, 0, Text.of(renderedText), !state.sneaking, state.light, state.squaredDistanceToCamera, cameraRenderState);
            }
        }
    }
}
