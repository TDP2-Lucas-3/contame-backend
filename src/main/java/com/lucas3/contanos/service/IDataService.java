package com.lucas3.contanos.service;

import com.lucas3.contanos.entities.Incident;
import com.lucas3.contanos.model.filters.DataFilter;
import com.lucas3.contanos.model.response.StateDataResponse;

import java.text.ParseException;
import java.util.List;

public interface IDataService {

    void loadData();

    StateDataResponse getStatesData(DataFilter dataFilter) throws ParseException;

    List<Incident> getIncidents(String category);


}
