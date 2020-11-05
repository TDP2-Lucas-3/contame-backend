package com.lucas3.contanos.model.filters;

import com.lucas3.contanos.entities.Category;

public class IncidentFilter {

    private String hood;
    private String category;

    public IncidentFilter() {
    }

    public IncidentFilter(String hood, String category) {
        this.hood = hood;
        this.category = category;
    }

    public String getHood() {
        return hood;
    }

    public void setHood(String hood) {
        this.hood = hood;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
