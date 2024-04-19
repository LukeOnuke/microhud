package com.lukeonuke.minihud.renderer.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.entity.Entity;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

import java.util.Objects;

public class MHFacingModule implements MicroHudRendererModule{
    private final MinecraftClient client = MinecraftClient.getInstance();
    @Override
    public String getName() {
        return "Current direction and p/y.";
    }

    @Override
    public String render(float deltaTick) {
        Entity entity = client.getCameraEntity();
        if(Objects.isNull(entity)) return "N/A";
        return String.format("P:%.2f Y:%.2f - %s%s", MathHelper.wrapDegrees(entity.getPitch()), MathHelper.wrapDegrees(entity.getYaw()), Formatting.BOLD, entity.getHorizontalFacing().asString());
    }

    @Override
    public void onEnable(boolean enabled) {

    }
}
