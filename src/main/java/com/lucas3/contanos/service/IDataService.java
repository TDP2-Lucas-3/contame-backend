package com.lucas3.contanos.service;

import com.lucas3.contanos.model.response.StateDataResponse;

public interface IDataService {

    void loadData();

    StateDataResponse getStatesData();


}
