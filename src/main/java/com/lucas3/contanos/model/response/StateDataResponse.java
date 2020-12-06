package com.lucas3.contanos.model.response;

import java.util.List;

public class StateDataResponse {

    private List<StateData> data;

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
}
