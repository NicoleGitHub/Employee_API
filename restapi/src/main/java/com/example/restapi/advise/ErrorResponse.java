package com.example.restapi.advise;

public class ErrorResponse {

    private String message;
    private int code;

    public ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}