package com.lukeonuke.minihud.mixin;

import com.lukeonuke.minihud.data.MicroHudOptions;
import com.lukeonuke.minihud.service.NameLookupData;
import com.lukeonuke.minihud.service.NameLookupService;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
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
import java.util.UUID;

@Mixin(net.minecraft.client.render.entity.EntityRenderer.class)
public abstract class EntityRenderer<T extends net.minecraft.entity.Entity, S extends net.minecraft.client.render.entity.state.EntityRenderState> {
    @Shadow
    public abstract TextRenderer getTextRenderer();

    @Unique
    private final NameLookupService nameLookupService = NameLookupService.getInstance();
    @Unique
    private final MicroHudOptions options = MicroHudOptions.getInstance();

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"), method = "renderLabelIfPresent")
    protected void renderLabelIfPresent(S state, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (state instanceof PlayerEntityRenderState) {
            if (options.getEnabledPlayerDiscordTag()) {
                TextRenderer renderer = getTextRenderer();
                String renderedText = Formatting.RED + "none";

                NameLookupData data = nameLookupService.getPlayerData(UUID.nameUUIDFromBytes(new byte[]{(byte) (((PlayerEntityRenderState) state).id)}).toString());
                if (Objects.nonNull(data)) {
                    renderedText = Formatting.DARK_GREEN + data.getTag();
                }
                renderer.draw(Text.of(renderedText), -renderer.getWidth(renderedText) / 2F, renderer.fontHeight * 1.2F, 0xFFFFFF, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.SEE_THROUGH, 0x00FFFFFF, light, true);
            }
        }
    }
}
