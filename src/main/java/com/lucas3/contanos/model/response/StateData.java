package com.lucas3.contanos.model.response;

import java.util.List;

public class StateData {

    private String name;

    private List<CategoryData> categories;

    public StateData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CategoryData> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryData> categories) {
        this.categories = categories;
    }
}
