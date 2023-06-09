package com.lukeonuke.minihud.renderer.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.server.integrated.IntegratedServer;

import java.util.Collection;
import java.util.Objects;

public class MHPlayerCountModule implements MicroHudRendererModule{
    private final MinecraftClient client = MinecraftClient.getInstance();

    @Override
    public String getName() {
        return "Player count";
    }

    @Override
    public String render(float deltaTick) {
        if(Objects.isNull(client.player)) return "Can't get players";
        final Collection<PlayerListEntry> playerList =  client.player.networkHandler.getListedPlayerListEntries();
        return playerList.size() + " players online";
    }
}
