package com.lucas3.contanos.service;

import com.lucas3.contanos.entities.Category;
import com.lucas3.contanos.entities.Incident;
import com.lucas3.contanos.model.exception.FailedToLoadImageException;
import com.lucas3.contanos.model.request.CategoryRequest;
import com.lucas3.contanos.model.request.IncidentRequest;
import com.lucas3.contanos.model.exception.IncidentNotFoundException;

import java.util.List;

public interface IIncidentService {

    Incident createIncident(IncidentRequest request) throws FailedToLoadImageException;

    List<Incident> getAllIncidents();

    Incident getIncidentById(Long id) throws IncidentNotFoundException;

    List<Category> getCategories();

    Category createCategory(CategoryRequest request);

}
