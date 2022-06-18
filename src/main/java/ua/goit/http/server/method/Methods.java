package ua.goit.http.server.method;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Methods {
    public static String doGet(String url, int id) {
        HttpRequest getReq = HttpRequest.newBuilder()
                .uri(URI.create(url + "/" + id))
                .GET()
                .build();

        HttpResponse<String> response;
        HttpClient client = HttpClient.newHttpClient();
        try {
            response = client.send(getReq, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response.body();
    }
}
