package com.lucas3.contanos.model.response.geocoding;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReverseGeocodingResponse {

    private String display_name;
    private GeocodingAddress address;

    public ReverseGeocodingResponse() {
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public GeocodingAddress getAddress() {
        return address;
    }

    public void setAddress(GeocodingAddress address) {
        this.address = address;
    }
}
