package com.lucas3.contanos.model.response;

import com.lucas3.contanos.entities.Report;

public class ReportResponse {

    private Long id;
    private String title;
    private String description;
    private String location;
    private String image;

    public ReportResponse(Long id, String title, String description, String location) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
    }

    public ReportResponse(Report report){
        this.id = report.getId();
        this.title = report.getTitle();
        this.description = report.getDescription();
        this.location = report.getLocation();
        this.image = report.getImage();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
