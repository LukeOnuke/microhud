package com.lukeonuke.minihud.renderer.module;

import java.time.Instant;

public class MHFpsRendererModule implements MicroHudRendererModule{
    @Override
    public String getName() {
        return "FPS meter";
    }

    private int lastFrameRate = 0;
    private int frameCounter = 0;
    private Instant lastFrame = Instant.now();
    @Override
    public String render(float deltaTick) {
        frameCounter++;
        if(Instant.now().toEpochMilli() - lastFrame.toEpochMilli() > 1000) {
            lastFrame = Instant.now();
            lastFrameRate = frameCounter;
            frameCounter = 0;
        }
        return lastFrameRate + " fps";
    }
}
