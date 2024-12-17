package com.lukeonuke.minihud.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lukeonuke.minihud.MicroHud;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class NameLookupService {
    private static NameLookupService instance;
    private final HttpClient client;

    private final ConcurrentHashMap<String, NameLookupData> cache = new ConcurrentHashMap<>();
    private final Vector<String> waiting = new Vector<>();

    private NameLookupService() {
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public static NameLookupService getInstance() {
        if (instance == null) instance = new NameLookupService();
        return instance;
    }

    public NameLookupData getPlayerData(String uuid) {
        if (!cache.containsKey(uuid) && !waiting.contains(uuid)) {
            waiting.add(uuid);
            MicroHud.LOGGER.info("Player joined {}", uuid);
            String url = "https://cache.samifying.com/api/data/uuid/" + uuid;
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
            client.sendAsync(req, HttpResponse.BodyHandlers.ofString()).thenAccept(response -> {
                // Generating response based of status codes
                if (response.statusCode() != 200) return;
                // Response is OK
                try {
                    cache.put(uuid, new Gson().fromJson(response.body(), NameLookupData.class));
                    waiting.remove(uuid);
                } catch (JsonSyntaxException e) {
                    MicroHud.LOGGER.error("EXCEPTION : " + e);
                }

            });
        }
        return cache.get(uuid);
    }
}
