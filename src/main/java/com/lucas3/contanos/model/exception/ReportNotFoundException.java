package com.lucas3.contanos.model.exception;

public class ReportNotFoundException extends Exception{

    private String message;

    public ReportNotFoundException(String message) {
        this.message= message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
