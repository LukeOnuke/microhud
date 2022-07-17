package com.lukeonuke.minihud.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lukeonuke.minihud.MicroHud;
import com.lukeonuke.minihud.data.type.MicroHudOptionsHolder;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MicroHudOptions {
    private static MicroHudOptions instance = null;
    private final Gson gson;

    public static MicroHudOptions getInstance() {
        if (instance == null) instance = new MicroHudOptions();

        return instance;
    }

    private MicroHudOptions() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        refresh();
    }

    private final MinecraftClient client = MinecraftClient.getInstance();
    private final File propertiesFile = client.runDirectory.toPath().resolve("microhud.json").normalize().toFile();
    private MicroHudOptionsHolder optionsHolder;

    public void refresh() {
        MicroHud.LOGGER.info("Run dir {}", MinecraftClient.getInstance().runDirectory.getPath());

        load();
        save();
    }

    public void load(){
        if(!propertiesFile.exists()) return;
        try {
            FileReader reader = new FileReader(propertiesFile, StandardCharsets.UTF_8);
            optionsHolder = gson.fromJson(reader, MicroHudOptionsHolder.class);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            if (!propertiesFile.exists()) {
                propertiesFile.createNewFile();
                optionsHolder = MicroHudOptionsHolder.getDefault();
            }

            FileWriter writer = new FileWriter(propertiesFile);
            gson.toJson(optionsHolder, writer);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getEnabledRendererModules() {
        return optionsHolder.getEnabledRenderers();
    }

    public void setEnabledRendererModules(ArrayList<String> enabledRendererModules) {
        optionsHolder.setEnabledRenderers(enabledRendererModules);
    }

    public int getSchema() {
        return optionsHolder.getSchema();
    }
}
