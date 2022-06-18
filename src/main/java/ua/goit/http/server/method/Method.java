package ua.goit.http.server.method;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public enum Method {
    GET("get"),
    POST("post"),
    PUT("put"),
    DELETE("delete");
    String text;

    Method(String text) {
        this.text = text;
    }
}
