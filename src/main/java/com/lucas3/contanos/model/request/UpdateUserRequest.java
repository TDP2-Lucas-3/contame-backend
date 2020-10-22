package com.lucas3.contanos.model.request;

public class UpdateUserRequest {

    private Long id;
    private String name;
    private String surname;
    private String photo;

    public UpdateUserRequest() {
    }

    public UpdateUserRequest(Long id, String name, String surname, String photo) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.photo = photo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
