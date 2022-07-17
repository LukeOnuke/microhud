package com.lukeonuke.minihud.renderer.module;

import net.minecraft.client.MinecraftClient;

import java.util.Objects;

public class MHPlayerCountModule implements MicroHudRendererModule{
    private final MinecraftClient client = MinecraftClient.getInstance();
    @Override
    public String getName() {
        return "Player count";
    }

    @Override
    public String render(float deltaTick) {
        if(Objects.isNull(client.world)) return "<couldn't get players>";
        return String.valueOf(client.world.getPlayers().size());
    }
}
