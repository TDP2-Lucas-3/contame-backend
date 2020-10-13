package com.lucas3.contanos.model.request;


import javax.validation.constraints.NotNull;
import java.util.List;

public class IncidentRequest {

    private String title;
    private String description;
    private double lat;
    private double lon;
    private List<String> images;
    private String category;

    public IncidentRequest() {
    }

    public IncidentRequest(String title, String description, double lat, double lon, List<String> images, String category) {
        this.title = title;
        this.description = description;
        this.lat = lat;
        this.lon = lon;
        this.images = images;
        this.category = category;

    }

    public IncidentRequest(String title, String category) {
        this.title = title;
        this.category = category;

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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
