package com.lucas3.contanos.repository;

import com.lucas3.contanos.entities.Incident;
import com.lucas3.contanos.model.filters.IncidentFilter;

import java.util.List;

public interface IncidentRepositoryCustom {

    List<Incident> findAll(IncidentFilter filter);
}
