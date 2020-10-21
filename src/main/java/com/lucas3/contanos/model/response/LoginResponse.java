package com.lucas3.contanos.model.response;

import java.util.List;

public class LoginResponse {

    private String token;
    private Long id;
    private String email;
    private String rol;

    public LoginResponse() {
    }

    public LoginResponse(String token, Long id, String email, String rol) {
        this.token = token;
        this.id = id;

        this.email = email;
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
