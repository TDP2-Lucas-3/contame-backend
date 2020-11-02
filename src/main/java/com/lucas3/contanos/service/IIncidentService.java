package com.lucas3.contanos.service;

import com.lucas3.contanos.entities.*;
import com.lucas3.contanos.model.exception.*;
import com.lucas3.contanos.model.request.CategoryRequest;
import com.lucas3.contanos.model.request.CommentRequest;
import com.lucas3.contanos.model.request.IncidentRequest;

import java.util.List;

public interface IIncidentService {

    Incident createIncident(IncidentRequest request, String email) throws FailedToLoadImageException, FailedReverseGeocodeException;

    List<Incident> getAllIncidents();
    List<Incident> getAllIncidents(String email) throws UserNotFoundException;

    List<Incident> getAllIncidentsByUser(String email);

    Incident getIncidentById(Long id) throws IncidentNotFoundException;

    List<Category> getCategories();

    Category createCategory(CategoryRequest request);

    Comment createComment(CommentRequest request, Long idIncident, String email) throws UserNotFoundException, IncidentNotFoundException;

    List<Comment> getComments(Long idIncident) throws IncidentNotFoundException;

    Vote vote(Long idIncident, String email) throws UserNotFoundException, IncidentNotFoundException;

    void unvote(Long idIncident, String email) throws UserNotFoundException, IncidentNotFoundException, VoteNotFoundException;

}
