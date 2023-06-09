package com.lukeonuke.minihud.data.type;

import com.google.common.reflect.Reflection;
import com.lukeonuke.minihud.renderer.MicroHudRenderer;
import com.lukeonuke.minihud.renderer.module.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class MicroHudOptionsHolder {
    private ArrayList<String> enabledRenderers;
    private int schema;

    public boolean enabledPlayerDiscordTag;

    public MicroHudOptionsHolder() {

    }

    public static MicroHudOptionsHolder getDefault(){
        MicroHudOptionsHolder optionsHolder = new MicroHudOptionsHolder();
        optionsHolder.setSchema(2);
        ArrayList<String> renderers = new ArrayList<>();
        renderers.add(MHDateTimeRendererModule.class.getSimpleName());
        renderers.add(MHPlayerPositionRendererModule.class.getSimpleName());
        renderers.add(MHFpsRendererModule.class.getSimpleName());
        renderers.add(MHLookingAtRenderModule.class.getSimpleName());
        optionsHolder.setEnabledRenderers(renderers);
        optionsHolder.enabledPlayerDiscordTag = true;
        return optionsHolder;
    }

    public ArrayList<String> getEnabledRenderers() {
        return enabledRenderers;
    }

    public void setEnabledRenderers(ArrayList<String> enabledRenderers) {
        this.enabledRenderers = enabledRenderers;
    }

    public int getSchema() {
        return schema;
    }

    public void setSchema(int schema) {
        this.schema = schema;
    }
}
