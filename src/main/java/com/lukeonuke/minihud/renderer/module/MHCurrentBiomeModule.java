package com.lukeonuke.minihud.renderer.module;

import com.lukeonuke.minihud.service.NameLookupService;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Formatting;
import net.minecraft.world.biome.Biome;

import java.util.Objects;

public class MHCurrentBiomeModule implements MicroHudRendererModule{
    private final MinecraftClient client = MinecraftClient.getInstance();

    private final String DEFAULT_COLUMN = "Biome : N/A";

    @Override
    public String getName() {
        return "Current biome";
    }

    @Override
    public String render(float deltaTick) {
        if (Objects.isNull(client.world)) return DEFAULT_COLUMN;
        if (Objects.isNull(client.player)) return DEFAULT_COLUMN;

        return getBiomeString(client.world.getBiome(client.player.getBlockPos())).split(":")[1];
    }

    @Override
    public void onEnable(boolean enabled) {

    }

    private static String getBiomeString(RegistryEntry<Biome> biome) {
        return (String)biome.getKeyOrValue().map((biomeKey) -> {
            return biomeKey.getValue().toString();
        }, (biome_) -> {
            return "[unregistered " + biome_ + "]";
        });
    }
}
