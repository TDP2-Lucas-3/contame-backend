package com.lucas3.contanos.model;

import com.lucas3.contanos.entity.Report;

public class ReportResponse {

    private Long id;
    private String title;
    private String description;

    public ReportResponse(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public ReportResponse(Report report){
        this.id = report.getId();
        this.title = report.getTitle();
        this.description = report.getDescription();
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
}
