package com.lucas3.contanos.model.filters;


public class DataFilter {

    private String hood;

    private String from;

    private String to;

    public DataFilter() {
    }

    public DataFilter(String hood, String from, String to) {
        this.hood = hood;
        this.from = from;
        this.to = to;
    }

    public String getHood() {
        return hood;
    }

    public void setHood(String hood) {
        this.hood = hood;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
