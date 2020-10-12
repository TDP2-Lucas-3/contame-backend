package com.lucas3.contanos.model.request;

public class ReportRequest {

    private String title;
    private String description;
    private String location;


    public ReportRequest(String title, String description, String location) {
        this.title = title;
        this.description = description;
        this.location = location;
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
}
