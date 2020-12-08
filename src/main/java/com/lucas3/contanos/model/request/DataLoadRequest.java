package com.lucas3.contanos.model.request;

public class DataLoadRequest {

    private Integer size;

    private double lat;

    private double lon;

    private Integer index;

    public DataLoadRequest() {
    }

    public DataLoadRequest(Integer size, double lat, double lon, Integer index) {
        this.size = size;
        this.lat = lat;
        this.lon = lon;
        this.index = index;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
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

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
