package com.lukeonuke.minihud.renderer.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;

import java.util.Objects;

public class MHServerBrandModule implements MicroHudRendererModule {
    private final MinecraftClient client = MinecraftClient.getInstance();
    @Override
    public String getName() {
        return "Server brand";
    }

    @Override
    public String render(float deltaTick) {
        if (Objects.isNull(client.getNetworkHandler())) return "<couldn't get server brand>";
        return "Server brand: " + Formatting.BOLD +  client.getNetworkHandler().getBrand();
    }

    @Override
    public void onEnable(boolean enabled) {

    }
}
