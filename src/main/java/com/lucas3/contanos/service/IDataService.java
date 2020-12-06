package com.lucas3.contanos.service;

import com.lucas3.contanos.model.filters.DataFilter;
import com.lucas3.contanos.model.response.StateDataResponse;

import java.text.ParseException;

public interface IDataService {

    void loadData();

    StateDataResponse getStatesData(DataFilter dataFilter) throws ParseException;


}
