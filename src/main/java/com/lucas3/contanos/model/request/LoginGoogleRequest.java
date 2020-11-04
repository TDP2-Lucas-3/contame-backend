package com.lucas3.contanos.model.request;

public class LoginGoogleRequest {

    private String token;

    private String firebaseToken;

    public LoginGoogleRequest() {
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }
}
