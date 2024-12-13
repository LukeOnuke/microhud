package com.lukeonuke.minihud.renderer.module;

import net.minecraft.client.MinecraftClient;

public class MHPlayerPositionRendererModule implements MicroHudRendererModule{
    private final MinecraftClient MINECRAFT_CLIENT = MinecraftClient.getInstance();
    @Override
    public String getName() {
        return "Player position";
    }

    @Override
    public String render(float deltaTick) {
        if(MINECRAFT_CLIENT.player == null) return null;
        return MINECRAFT_CLIENT.player.getBlockPos().toShortString();
    }

    @Override
    public void onEnable(boolean enabled) {

    }
}
