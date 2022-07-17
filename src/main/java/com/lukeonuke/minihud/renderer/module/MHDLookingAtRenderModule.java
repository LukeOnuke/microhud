package com.lukeonuke.minihud.renderer.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.Objects;

public class MHDLookingAtRenderModule implements MicroHudRendererModule{
    private final MinecraftClient client = MinecraftClient.getInstance();
    @Override
    public String getName() {
        return "Looking at block";
    }

    @Override
    public String render(float deltaTick) {
        if(Objects.isNull(client.player)) return null;
        if(Objects.isNull(client.world)) return null;

        HitResult hitResult = client.player.raycast(20D, 0F, false);

        if(!hitResult.getType().equals(HitResult.Type.BLOCK)) return "<no block>";

        BlockHitResult blockHitResult = (BlockHitResult) hitResult;
        hitResult = null; //flag for gcc

        return "(" + blockHitResult.getBlockPos().toShortString() + ") " + Formatting.BOLD + client.world.getBlockState(blockHitResult.getBlockPos()).getBlock().getName().getString();
    }
}
