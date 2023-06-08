package com.lukeonuke.minihud.renderer.module;

public interface MicroHudRendererModule {
    String getName();
    String render(float deltaTick);
}
