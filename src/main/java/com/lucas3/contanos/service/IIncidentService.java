package com.lucas3.contanos.service;

import com.lucas3.contanos.entities.*;
import com.lucas3.contanos.model.exception.*;
import com.lucas3.contanos.model.filters.IncidentFilter;
import com.lucas3.contanos.model.request.ChangeStateRequest;
import com.lucas3.contanos.model.request.CommentRequest;
import com.lucas3.contanos.model.request.IncidentRequest;
import com.lucas3.contanos.model.response.ContameMapResponse;

import java.util.List;

public interface IIncidentService {

    Incident createIncident(IncidentRequest request, String email) throws FailedToLoadImageException, FailedReverseGeocodeException, UserNotFoundException;

    List<Incident> getAllIncidents();
    List<Incident> getAllIncidents(String email) throws UserNotFoundException;

    List<Incident> getAllIncidentsByUser(String email) throws UserNotFoundException;

    Incident getIncidentById(Long id, String email) throws IncidentNotFoundException, UserNotFoundException;

    Incident getIncidentById(Long id) throws IncidentNotFoundException;


    List<String> getCategories();

    List<ContameMapResponse> getCategoriesMap();

    List<String> getSubcategories(String category);


    Comment createCommentUser(CommentRequest request, Long idIncident, String email) throws UserNotFoundException, IncidentNotFoundException;

    Comment createCommentAdmin(CommentRequest request, Long idIncident, String email) throws UserNotFoundException, IncidentNotFoundException, CategoryNotFoundException, InvalidCategoryException;

    List<Comment> getComments(Long idIncident) throws IncidentNotFoundException, UserNotFoundException;

    List<Comment> getPublicComments(Long idIncident) throws IncidentNotFoundException, UserNotFoundException;

    List<Comment> getPrivateComments(Long idIncident) throws IncidentNotFoundException;

    Vote vote(Long idIncident, String email) throws UserNotFoundException, IncidentNotFoundException, InvalidVoteException;

    void unvote(Long idIncident, String email) throws UserNotFoundException, IncidentNotFoundException, VoteNotFoundException;

    List<ContameMapResponse> getStatesPublic();

    List<ContameMapResponse> getStatesPrivate();

    void changeState(Long id, ChangeStateRequest request, String email) throws IncidentNotFoundException, StateNotFoundException, UserNotFoundException;

    void changeStatePrivate(Long id, ChangeStateRequest request, String email) throws IncidentNotFoundException, StateNotFoundException, UserNotFoundException;

    void setFather(Long idSon, Long idFather) throws IncidentSonNotFoundException, IncidentFatherNotFoundException, SonHaveSonsException;

    Incident getFather(Long id) throws IncidentSonNotFoundException, IncidentFatherNotFoundException;

    List<Incident> getSons(Long id) throws IncidentFatherNotFoundException, IncidentSonNotFoundException;

}
