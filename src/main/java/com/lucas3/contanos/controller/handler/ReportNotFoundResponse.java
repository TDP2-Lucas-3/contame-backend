package com.lucas3.contanos.controller.handler;

public class ReportNotFoundResponse {

    private String message;

    public ReportNotFoundResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
