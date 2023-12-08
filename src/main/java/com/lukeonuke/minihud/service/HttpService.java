package com.lukeonuke.minihud.service;

import com.google.gson.Gson;
import com.lukeonuke.minihud.data.type.HttpError;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpService {
    private final HttpClient client;
    private static HttpService instance;
    private final Gson gson = new Gson();

    private HttpService() {
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(3))
                .build();
    }

    public static HttpService getInstance() {
        if (instance == null) {
            instance = new HttpService();
        }
        return instance;
    }

    public String get(String url) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> rsp = client.send(req, HttpResponse.BodyHandlers.ofString());

        // Generating response based of status codes
        int code = rsp.statusCode();
        if (code == 200) {
            // Response is OK
            return rsp.body();
        }
        if (code == 400) {
            throw new RuntimeException(gson.fromJson(rsp.body(), HttpError.class).getMessage());
        }
        throw new RuntimeException("Response code " + code);
    }

    public void post(String url, Object body){
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(body)))
                .build();

        client.sendAsync(req, HttpResponse.BodyHandlers.ofString());
    }
}
