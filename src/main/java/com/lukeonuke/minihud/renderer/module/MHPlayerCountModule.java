package com.lukeonuke.minihud.renderer.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.server.integrated.IntegratedServer;

import java.util.Objects;

public class MHPlayerCountModule implements MicroHudRendererModule{
    private final MinecraftClient client = MinecraftClient.getInstance();
    @Override
    public String getName() {
        return "Player count";
    }

    @Override
    public String render(float deltaTick) {
        final IntegratedServer server =  client.getServer();
        if(Objects.isNull(server)) return "N/A / N/A players";
        return server.getCurrentPlayerCount() + "/" + server.getMaxPlayerCount();
    }
}
