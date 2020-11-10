package com.lucas3.contanos.service;

import com.lucas3.contanos.entities.*;
import com.lucas3.contanos.model.exception.*;
import com.lucas3.contanos.model.filters.IncidentFilter;
import com.lucas3.contanos.model.firebase.PushNotificationRequest;
import com.lucas3.contanos.model.request.CategoryRequest;
import com.lucas3.contanos.model.request.CommentRequest;
import com.lucas3.contanos.model.request.IncidentRequest;
import com.lucas3.contanos.model.response.geocoding.LocationResponse;
import com.lucas3.contanos.repository.*;
import com.lucas3.contanos.service.firebase.FCMService;
import com.lucas3.contanos.service.firebase.NotificationService;
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

    @Autowired
    private NotificationService notificationService;


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
            LocationResponse location = geocodingService.getLocationFromCoordinates(request.getLat(), request.getLon());
            incident.setLocation(location.getAddress());
            incident.setHood(location.getHood());
        }
        User user = userRepository.findByEmail(email).get();
        incident.setUser(user);
        incident.setState(EIncidentState.REPORTADO);
        incidentRepository.save(incident);
        notificationService.sendIncidentNotification(user,incident);

        return incident;
    }



    @Override
    public List<Incident> getAllIncidents(String email, IncidentFilter filter) throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);

        if(!user.isPresent()) throw new UserNotFoundException();

        List<Incident> incidents = incidentRepository.findAll(filter);

        for (Incident incident: incidents) {
            incident.setVotes(voteRepository.countByIncident(incident));
            incident.setVoteByUser(voteRepository.findByUserAndIncident(user.get(),incident).isPresent());
        }

        return incidents;
    }

    @Override
    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    @Override
    public List<Incident> getAllIncidentsByUser(String email) throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);

        if(!user.isPresent()) throw new UserNotFoundException();

        List<Incident> incidents = incidentRepository.findAllByUser(userRepository.findByEmail(email).get());

        for (Incident incident: incidents) {
            incident.setVotes(voteRepository.countByIncident(incident));
            incident.setVoteByUser(voteRepository.findByUserAndIncident(user.get(),incident).isPresent());
        }
        return incidents;
    }

    @Override
    public Incident getIncidentById(Long id, String email) throws IncidentNotFoundException, UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if(!user.isPresent()) throw new UserNotFoundException();

        Optional<Incident> incident = incidentRepository.findById(id);
        if(incident.isPresent()){
            Incident inc = incident.get();
            inc.setVotes(voteRepository.countByIncident(inc));
            inc.setVoteByUser(voteRepository.findByUserAndIncident(user.get(),inc).isPresent());
            return inc;
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
        notificationService.sendVoteNotification(user.get(),incident.get());
        return vote;
    }

    @Override
    public void unvote(Long idIncident, String email) throws UserNotFoundException, IncidentNotFoundException, VoteNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        Optional<Incident> incident = incidentRepository.findById(idIncident);

        if(!user.isPresent()) throw new UserNotFoundException();
        if(!incident.isPresent()) throw new IncidentNotFoundException();

        Optional<Vote> vote = voteRepository.findByUserAndIncident(user.get(),incident.get());
        if(vote.isPresent()){
            voteRepository.delete(vote.get());
        }else{
            throw new VoteNotFoundException();
        }
    }

    @Override
    public List<EIncidentState> getStates() {
        return Arrays.asList(EIncidentState.values());
    }

    @Override
    public void changeState(Long id, String state) throws IncidentNotFoundException{
        Optional<Incident> incident = incidentRepository.findById(id);
        if(!incident.isPresent()) throw new IncidentNotFoundException();
        EIncidentState newState = EIncidentState.valueOf(state);
        incident.get().setState(newState);
        incident.get().setUpdateDate(new Date());
        incidentRepository.save(incident.get());
        notificationService.sendChangeStateNotification(incident.get());
    }


}
