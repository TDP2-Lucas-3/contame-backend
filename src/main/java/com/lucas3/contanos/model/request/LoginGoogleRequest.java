package com.lucas3.contanos.model.request;

public class LoginGoogleRequest {

    private String token;
    private String name;
    private String surname;
    private String photo;

    public LoginGoogleRequest() {
    }

    public LoginGoogleRequest(String token, String name, String surname, String photo) {
        this.token = token;
        this.name = name;
        this.surname = surname;
        this.photo = photo;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
