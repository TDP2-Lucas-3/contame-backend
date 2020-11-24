package com.lucas3.contanos.model.response;

public class ContameMapResponse {

    private String key;
    private String value;

    public ContameMapResponse(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public ContameMapResponse() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
