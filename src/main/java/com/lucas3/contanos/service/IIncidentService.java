package com.lucas3.contanos.service;

import com.lucas3.contanos.entities.*;
import com.lucas3.contanos.model.exception.*;
import com.lucas3.contanos.model.filters.IncidentFilter;
import com.lucas3.contanos.model.request.CategoryRequest;
import com.lucas3.contanos.model.request.CommentRequest;
import com.lucas3.contanos.model.request.IncidentRequest;

import java.util.List;

public interface IIncidentService {

    Incident createIncident(IncidentRequest request, String email) throws FailedToLoadImageException, FailedReverseGeocodeException;

    List<Incident> getAllIncidents();
    List<Incident> getAllIncidents(String email, IncidentFilter filter) throws UserNotFoundException;

    List<Incident> getAllIncidentsByUser(String email) throws UserNotFoundException;

    Incident getIncidentById(Long id, String email) throws IncidentNotFoundException, UserNotFoundException;

    List<Category> getCategories();

    Category createCategory(CategoryRequest request);

    Comment createComment(CommentRequest request, Long idIncident, String email) throws UserNotFoundException, IncidentNotFoundException;

    List<Comment> getComments(Long idIncident) throws IncidentNotFoundException;

    Vote vote(Long idIncident, String email) throws UserNotFoundException, IncidentNotFoundException, InvalidVoteException;

    void unvote(Long idIncident, String email) throws UserNotFoundException, IncidentNotFoundException, VoteNotFoundException;

    List<EIncidentState> getStates();

    void changeState(Long id, String state) throws IncidentNotFoundException;

}
