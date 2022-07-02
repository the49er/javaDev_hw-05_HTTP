package ua.goit.http.server.entity;

import lombok.Data;

@Data
public class ApiResponse {
    int code;
    String type;
    String message;
}
