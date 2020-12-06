package com.lucas3.contanos.model.response;

import java.util.List;

public class StateDataResponse {

    private List<StateData> data;

    private List<CategoryData> categoryTotals;

    public StateDataResponse() {
    }

    public StateDataResponse(List<StateData> data) {
        this.data = data;
    }

    public List<StateData> getData() {
        return data;
    }

    public void setData(List<StateData> data) {
        this.data = data;
    }

    public List<CategoryData> getCategoryTotals() {
        return categoryTotals;
    }

    public void setCategoryTotals(List<CategoryData> categoryTotals) {
        this.categoryTotals = categoryTotals;
    }
}
