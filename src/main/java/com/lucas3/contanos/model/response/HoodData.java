package com.lucas3.contanos.model.response;

public class HoodData {

    private String hood;

    private Integer value;

    public HoodData() {
    }

    public HoodData(String hood, Integer value) {
        this.hood = hood;
        this.value = value;
    }

    public String getHood() {
        return hood;
    }

    public void setHood(String hood) {
        this.hood = hood;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
