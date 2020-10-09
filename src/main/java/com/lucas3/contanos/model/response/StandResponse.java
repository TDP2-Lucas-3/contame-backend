package com.lucas3.contanos.model.response;

public class StandResponse {

    private String message;

    public StandResponse() {
    }

    public StandResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
