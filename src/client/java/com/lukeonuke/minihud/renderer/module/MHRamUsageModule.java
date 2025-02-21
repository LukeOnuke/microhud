package com.lukeonuke.minihud.renderer.module;

import net.minecraft.text.Text;

public class MHRamUsageModule implements MicroHudRendererModule {

    @Override
    public String getName() {
        return Text.translatable("gui.microhud.textrenderer.MHRamUsageModule").getString();
    }

    @Override
    public String render(float deltaTick) {
        final Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        return "%dmb/%dmb".formatted(bytesToMegabytes(maxMemory - runtime.freeMemory()), bytesToMegabytes(maxMemory));
    }

    @Override
    public void onEnable(boolean enabled) {

    }

    private long bytesToMegabytes(long bytes) {
        return bytes / 1048576L; //1024*1024 = 1048576
    }
}
