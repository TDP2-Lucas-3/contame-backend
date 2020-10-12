package com.lucas3.contanos.model.request;

import org.springframework.web.multipart.MultipartFile;

public class ReportRequest {

    private String title;
    private String description;
    private String location;
    private MultipartFile image;


    public ReportRequest(String title, String description, String location, MultipartFile image) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
