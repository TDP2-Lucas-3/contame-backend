package com.lucas3.contanos.repository;

import com.lucas3.contanos.entities.EIncidentCategory;
import com.lucas3.contanos.entities.EIncidentStatePublic;
import com.lucas3.contanos.entities.Incident;
import com.lucas3.contanos.model.filters.DataFilter;
import com.lucas3.contanos.model.filters.IncidentFilter;

import javax.xml.crypto.Data;
import java.text.ParseException;
import java.util.List;

public interface IncidentRepositoryCustom {

    List<Incident> findAll(IncidentFilter filter);


    Integer countByCategory(DataFilter filter, EIncidentCategory category) throws ParseException;

    Integer countByStateAndCategory(DataFilter filter, EIncidentStatePublic state, EIncidentCategory category);
}
