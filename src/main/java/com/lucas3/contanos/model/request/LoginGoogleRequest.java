package com.lucas3.contanos.model.request;

public class LoginGoogleRequest {

    private String token;

    public LoginGoogleRequest() {
    }

    public LoginGoogleRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
