package com.lukeonuke.minihud.data.type;

import com.google.common.reflect.Reflection;
import com.lukeonuke.minihud.renderer.MicroHudRenderer;
import com.lukeonuke.minihud.renderer.module.MicroHudRendererModule;

import java.util.ArrayList;

public class MicroHudOptionsHolder {
    private ArrayList<String> enabledRenderers;
    private int schema;

    public MicroHudOptionsHolder() {

    }

    public static MicroHudOptionsHolder getDefault(){
        MicroHudOptionsHolder optionsHolder = new MicroHudOptionsHolder();
        optionsHolder.setSchema(1);
        ArrayList<String> renderers = new ArrayList<>();
        MicroHudRenderer.getAvailableRendererModules().forEach(microHudRendererModule -> {
            renderers.add(microHudRendererModule.getClass().getSimpleName());
        });
        optionsHolder.setEnabledRenderers(renderers);
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
