package ua.goit.http.server.method;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Methods {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    public static String doGet(String url, String str) {
        HttpRequest getReq = HttpRequest.newBuilder()
                .uri(URI.create(url + str))
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = CLIENT.send(getReq, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response.body();
    }

    public static String doPost(String url, String body) {
        HttpRequest request;
        request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = null;
        try {
            response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return response.body();
    }

    public static String delete(String url){
        HttpRequest request;
        request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .header("api_key", "special-key")
                .build();
        HttpResponse<String> response = null;
        try {
            response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return response.body();
    }

    public static String put(String url, String body){
        HttpRequest request;
        request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .header("api_key", "special-key")
                .build();
        HttpResponse<String> response = null;
        try {
            response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return response.body();
    }
}
