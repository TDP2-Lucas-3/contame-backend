package com.lucas3.contanos.model.request.imgur;

import org.springframework.web.multipart.MultipartFile;

public class UploadImage {

    private String image;
    private String key;

    public UploadImage(String image, String key) {
        this.image = image;
        this.key = key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
