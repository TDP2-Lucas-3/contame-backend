package com.lucas3.contanos.model.request;

import org.springframework.web.multipart.MultipartFile;

public class ReportRequest {

    private String title;
    private String description;
    private String location;
    private MultipartFile file;


    public ReportRequest(String title, String description, String location, MultipartFile file) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.file = file;
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

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
