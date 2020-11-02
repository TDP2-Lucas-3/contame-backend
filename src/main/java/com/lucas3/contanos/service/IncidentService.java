package com.lucas3.contanos.service;

import com.lucas3.contanos.entities.*;
import com.lucas3.contanos.model.exception.FailedReverseGeocodeException;
import com.lucas3.contanos.model.exception.FailedToLoadImageException;
import com.lucas3.contanos.model.exception.UserNotFoundException;
import com.lucas3.contanos.model.request.CategoryRequest;
import com.lucas3.contanos.model.request.CommentRequest;
import com.lucas3.contanos.model.request.IncidentRequest;
import com.lucas3.contanos.model.exception.IncidentNotFoundException;
import com.lucas3.contanos.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class IncidentService implements IIncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImgbbService imgbbService;

    @Autowired
    private GeocodingService geocodingService;


    @Override
    public Incident createIncident(IncidentRequest request, String email) throws FailedToLoadImageException, FailedReverseGeocodeException {
        Optional<Category> category = categoryRepository.findById(request.getCategory());

        Incident incident = new Incident(request.getTitle(),category.get(),request.getDescription(), request.getLat(), request.getLon());

        List<String> imagesURLs = new ArrayList<>();
        if(request.getImages() != null){
            for (String imageBase64:request.getImages()) {
                imagesURLs.add(imgbbService.uploadImgToImgbb(imageBase64));
            }
            incident.setImages(imagesURLs);
        }
        if(request.getLat() != 0 && request.getLon() != 0){
            String location = geocodingService.getLocationFromCoordinates(request.getLat(), request.getLon());
            incident.setLocation(location);
        }
        incident.setUser(userRepository.findByEmail(email).get());
        incident.setState(EIncidentState.REPORTADO);
        incidentRepository.save(incident);
        return incident;
    }

    @Override
    public List<Incident> getAllIncidents(String email) throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);

        if(!user.isPresent()) throw new UserNotFoundException();

        List<Incident> incidents = incidentRepository.findAll();

        for (Incident incident: incidents) {
            incident.setVotes(voteRepository.countByIncident(incident));
            incident.setVoteByUser(voteRepository.findByUserAndIncident(user.get(),incident).isPresent());
        }

        return incidentRepository.findAll();
    }

    @Override
    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    @Override
    public List<Incident> getAllIncidentsByUser(String email) {
        return incidentRepository.findAllByUser(userRepository.findByEmail(email).get());
    }

    @Override
    public Incident getIncidentById(Long id) throws IncidentNotFoundException {
        Optional<Incident> incident = incidentRepository.findById(id);
        if(incident.isPresent()){
            return incident.get();
        }
        throw new IncidentNotFoundException("Reporte inexistente");
    }

    @Override
    public List<Category> getCategories() {
        List<Category> categories = categoryRepository.findAll() ;
        Collections.sort(categories, Comparator.comparing(Category::getName));
        return categories;
    }

    @Override
    public Category createCategory(CategoryRequest request) {
        return categoryRepository.save(new Category(request));
    }

    @Override
    public Comment createComment(CommentRequest request, Long idIncident, String email) throws UserNotFoundException, IncidentNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        Optional<Incident> incident = incidentRepository.findById(idIncident);

        if(!user.isPresent()) throw new UserNotFoundException();
        if(!incident.isPresent()) throw new IncidentNotFoundException();

        Comment comment = new Comment(request.getComment(),user.get(),incident.get());
        comment.setCategory(ECommentCategory.PUBLIC);
        commentRepository.save(comment);
        return comment;
    }

    @Override
    public List<Comment> getComments(Long idIncident) throws IncidentNotFoundException {
        Optional<Incident> incident = incidentRepository.findById(idIncident);
        if(!incident.isPresent()) throw new IncidentNotFoundException();

        return commentRepository.findAllByIncident(incident.get());
    }

    @Override
    public Vote vote(Long idIncident, String email) throws UserNotFoundException, IncidentNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        Optional<Incident> incident = incidentRepository.findById(idIncident);

        if(!user.isPresent()) throw new UserNotFoundException();
        if(!incident.isPresent()) throw new IncidentNotFoundException();

        Vote vote = new Vote(user.get(),incident.get());

        voteRepository.save(vote);

        return vote;
    }


}
