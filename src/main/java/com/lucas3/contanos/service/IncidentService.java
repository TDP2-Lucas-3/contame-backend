package com.lucas3.contanos.service;

import com.lucas3.contanos.entities.*;
import com.lucas3.contanos.model.exception.*;
import com.lucas3.contanos.model.filters.IncidentFilter;
import com.lucas3.contanos.model.firebase.PushNotificationRequest;
import com.lucas3.contanos.model.request.CategoryRequest;
import com.lucas3.contanos.model.request.ChangeStateRequest;
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
    public Incident createIncident(IncidentRequest request, String email) throws FailedToLoadImageException, FailedReverseGeocodeException, UserNotFoundException {
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
        User user = verifyUser(email);
        incident.setUser(user);
        incident.setState(EIncidentState.REPORTADO);
        incidentRepository.save(incident);
        notificationService.sendIncidentNotification(user,incident);

        return incident;
    }



    @Override
    public List<Incident> getAllIncidents(String email, IncidentFilter filter) throws UserNotFoundException {
        User user = verifyUser(email);
        List<Incident> incidents = incidentRepository.findAll(filter);

        for (Incident incident: incidents) {
            incident.setVotes(voteRepository.countByIncident(incident));
            incident.setVoteByUser(voteRepository.findByUserAndIncident(user,incident).isPresent());
        }

        return incidents;
    }

    @Override
    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    @Override
    public List<Incident> getAllIncidentsByUser(String email) throws UserNotFoundException {
        User user = verifyUser(email);

        List<Incident> incidents = incidentRepository.findAllByUser(userRepository.findByEmail(email).get());

        for (Incident incident: incidents) {
            incident.setVotes(voteRepository.countByIncident(incident));
            incident.setVoteByUser(voteRepository.findByUserAndIncident(user,incident).isPresent());
        }
        return incidents;
    }

    @Override
    public Incident getIncidentById(Long id, String email) throws IncidentNotFoundException, UserNotFoundException {
        User user = verifyUser(email);
        Incident incident = verifyIncident(id);

        incident.setVotes(voteRepository.countByIncident(incident));
        incident.setVoteByUser(voteRepository.findByUserAndIncident(user,incident).isPresent());
        List<Comment> comments = new ArrayList<>(getPublicComments(id));
        incident.setComments(comments);
        return incident;
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
    public Comment createCommentUser(CommentRequest request, Long idIncident, String email) throws UserNotFoundException, IncidentNotFoundException {
        User user = verifyUser(email);
        Incident incident = verifyIncident(idIncident);

        Comment comment = new Comment(request.getComment(),user,incident);
        comment.setCategory(ECommentCategory.PUBLIC);
        commentRepository.save(comment);
        notificationService.sendCommentUserNotification(user,incident);
        return comment;
    }

    @Override
    public Comment createCommentAdmin(CommentRequest request, Long idIncident, String email) throws UserNotFoundException, IncidentNotFoundException, CategoryNotFoundException, InvalidCategoryException {
        ECommentCategory category = null;

        User user = verifyUser(email);

        Incident incident = verifyIncident(idIncident);

        if(request.getCategory() == null) throw new CategoryNotFoundException();
        try{
            category = ECommentCategory.valueOf(request.getCategory().toUpperCase());
        }catch(IllegalArgumentException e){
            throw new InvalidCategoryException();
        }

        Comment comment = new Comment(request.getComment(),user,incident);
        comment.setCategory(category);
        commentRepository.save(comment);

        if (category.equals(ECommentCategory.PUBLIC)){
            notificationService.sendCommentAdminNotification(incident);
        }
        return comment;
    }

    @Override
    public List<Comment> getComments(Long idIncident) throws IncidentNotFoundException, UserNotFoundException {
        Incident incident = verifyIncident(idIncident);

       List<Comment> comments = commentRepository.findAllByIncident(incident);
        for (Comment comment: comments) {
            if(comment.getUser().getId().equals(incident.getUser().getId())){
                comment.setOwner(true);
            }else{
                comment.setOwner(false);
            }
        }
        return comments;
    }

    @Override
    public List<Comment> getPublicComments(Long idIncident) throws IncidentNotFoundException, UserNotFoundException {
        Incident incident = verifyIncident(idIncident);

        List<Comment> comments = commentRepository.findAllByIncidentAndCategory(incident, ECommentCategory.PUBLIC);
        for (Comment comment: comments) {
            if(comment.getUser().getId().equals(incident.getUser().getId())){
                comment.setOwner(true);
            }else{
                comment.setOwner(false);
            }
        }
        return comments;
    }

    @Override
    public List<Comment> getPrivateComments(Long idIncident) throws IncidentNotFoundException {
        Incident incident = verifyIncident(idIncident);
        return commentRepository.findAllByIncidentAndCategory(incident, ECommentCategory.PRIVATE);
    }

    @Override
    public Vote vote(Long idIncident, String email) throws UserNotFoundException, IncidentNotFoundException, InvalidVoteException {

        User user = verifyUser(email);

        Incident incident = verifyIncident(idIncident);

        if(user.getEmail().equals(incident.getUser().getEmail())) throw new InvalidVoteException();

        Vote vote = new Vote(user,incident);
        voteRepository.save(vote);
        return vote;
    }

    @Override
    public void unvote(Long idIncident, String email) throws UserNotFoundException, IncidentNotFoundException, VoteNotFoundException {

        User user = verifyUser(email);
        Incident incident = verifyIncident(idIncident);

        Optional<Vote> vote = voteRepository.findByUserAndIncident(user,incident);
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
    public void changeState(Long id, ChangeStateRequest request, String email) throws IncidentNotFoundException, UserNotFoundException {
        Incident incident = verifyIncident(id);

        EIncidentState newState = EIncidentState.valueOf(request.getState());

        changeStateIncident(incident,newState);

        if(incident.getParent() != null){
            changeStateFamily(incident,newState);
        }else{
            changeStateSons(incident,newState);
        }

        postCommentAdmin(request.getComment(),email, incident);
    }

    private void changeStateSons(Incident incident, EIncidentState newState){
        List<Incident> sons = incidentRepository.findAllByParent(incident);
        for (Incident son: sons) {
            changeStateIncident(son, newState);
        }
    }
    private void changeStateFamily(Incident incident, EIncidentState newState){
        List<Incident> family = incidentRepository.findAllByParent(incident.getParent());
        for (Incident familiar: family) {
            if(!incident.getId().equals(familiar.getId())){
                changeStateIncident(familiar, newState);
            }
        }
        changeStateIncident(incident.getParent(), newState);
    }

    private void changeStateIncident(Incident incident, EIncidentState state){
        if(state.equals(EIncidentState.RESUELTO) || state.equals(EIncidentState.ARCHIVADO)  ){
            incident.setCompleteDate(new Date());
        }
        incident.setState(state);
        incident.setUpdateDate(new Date());
        incidentRepository.save(incident);
        notificationService.sendChangeStateNotification(incident);

    }

    private void postCommentAdmin(String commentary, String email, Incident incident) throws UserNotFoundException {
        User user = verifyUser(email);
        Comment comment = new Comment(commentary,user,incident);
        comment.setCategory(ECommentCategory.PRIVATE);
        commentRepository.save(comment);
    }

    @Override
    public void setFather(Long idSon, Long idFather) throws IncidentSonNotFoundException, IncidentFatherNotFoundException, SonHaveSonsException {
        Optional<Incident> son = incidentRepository.findById(idSon);
        if(!son.isPresent()) throw new IncidentSonNotFoundException();

        Optional<Incident> father= incidentRepository.findById(idFather);
        if(!father.isPresent()) throw new IncidentFatherNotFoundException();

        if(!sonHaveNoSons(son.get())) throw new SonHaveSonsException();

        son.get().setParent(father.get());
        incidentRepository.save(son.get());

    }

    @Override
    public Incident getFather(Long id) throws IncidentSonNotFoundException, IncidentFatherNotFoundException {
        Optional<Incident> son = incidentRepository.findById(id);
        if(!son.isPresent()) throw new IncidentSonNotFoundException();

        if(son.get().getParent() == null) throw new IncidentFatherNotFoundException();

        return son.get().getParent();
    }

    @Override
    public List<Incident> getSons(Long id) throws IncidentFatherNotFoundException, IncidentSonNotFoundException {
        Optional<Incident> father = incidentRepository.findById(id);
        if(!father.isPresent()) throw new IncidentFatherNotFoundException();

        List<Incident> sons = incidentRepository.findAllByParent(father.get());
        if( sons.isEmpty()) throw new IncidentSonNotFoundException();

        return sons;

    }

    private boolean sonHaveNoSons(Incident son) {
        List<Incident> sons = incidentRepository.findAllByParent(son);
        return sons.isEmpty();
    }

    private User verifyUser(String email) throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if(!user.isPresent()) throw new UserNotFoundException();
        return user.get();
    }

    private Incident verifyIncident(Long id) throws IncidentNotFoundException {
        Optional<Incident> incident = incidentRepository.findById(id);
        if(!incident.isPresent()) throw new IncidentNotFoundException();
        return incident.get();
    }


}
