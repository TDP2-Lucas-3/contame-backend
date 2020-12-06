package com.lucas3.contanos.model.filters;


public class DataFilter {

    private String hood;

    private String createDate;

    private String completeDate;

    public DataFilter() {
    }

    public DataFilter(String hood, String createDate, String completeDate) {
        this.hood = hood;
        this.createDate = createDate;
        this.completeDate = completeDate;
    }

    public String getHood() {
        return hood;
    }

    public void setHood(String hood) {
        this.hood = hood;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }
}
