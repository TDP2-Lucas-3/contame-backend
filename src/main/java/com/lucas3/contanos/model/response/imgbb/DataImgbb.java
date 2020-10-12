package com.lucas3.contanos.model.response.imgbb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataImgbb {
    private String url;

    public DataImgbb() {
    }

    public DataImgbb(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
