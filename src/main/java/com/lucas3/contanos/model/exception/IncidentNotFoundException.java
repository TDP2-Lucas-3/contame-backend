package com.lucas3.contanos.model.exception;

public class IncidentNotFoundException extends Exception{

    private String message;

    public IncidentNotFoundException() {
    }

    public IncidentNotFoundException(String message) {
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
