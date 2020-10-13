package com.lucas3.contanos.controller.handler;

public class IncidentNotFoundResponse {

    private String message;

    public IncidentNotFoundResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
