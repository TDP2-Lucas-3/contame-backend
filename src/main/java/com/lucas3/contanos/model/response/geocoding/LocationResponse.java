package com.lucas3.contanos.model.response.geocoding;

public class LocationResponse {

    private String address;

    private String hood;

    public LocationResponse() {
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHood() {
        return hood;
    }

    public void setHood(String hood) {
        this.hood = hood;
    }
}
