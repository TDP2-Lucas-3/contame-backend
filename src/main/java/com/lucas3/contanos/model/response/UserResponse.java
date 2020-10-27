package com.lucas3.contanos.model.response;

import com.lucas3.contanos.entities.User;

import java.util.Date;
import java.util.List;

public class UserResponse {

    private Long id;
    private String email;
    private String rol;
    private String name;
    private String surname;
    private String photo;
    private Date registerDate;
    private Date lastLoginDate;
    private Integer incidentCount;
    private String state;

    public UserResponse() {
    }

    public UserResponse(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.rol = user.getRol().toString();
        this.name = user.getProfile().getName();
        this.surname = user.getProfile().getSurename();
        this.photo = user.getProfile().getPhoto();
        this.registerDate = user.getRegisterDate();
        this.lastLoginDate = user.getLastLoginDate();
        this.state = user.getUserState().toString();
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

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public Integer getIncidentCount() {
        return incidentCount;
    }

    public void setIncidentCount(Integer incidentCount) {
        this.incidentCount = incidentCount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
