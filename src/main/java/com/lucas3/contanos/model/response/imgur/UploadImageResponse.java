package com.lucas3.contanos.model.response.imgur;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadImageResponse {

    private String status;
    private boolean success;

    private DataImgbb data;

    public UploadImageResponse() {
    }

    public UploadImageResponse(String status, boolean success, DataImgbb data) {
        this.status = status;
        this.success = success;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public DataImgbb getData() {
        return data;
    }

    public void setData(DataImgbb data) {
        this.data = data;
    }
}
