package com.lucas3.contanos.service;

import com.lucas3.contanos.entities.*;
import com.lucas3.contanos.model.exception.*;
import com.lucas3.contanos.model.filters.IncidentFilter;
import com.lucas3.contanos.model.request.ChangeStateRequest;
import com.lucas3.contanos.model.request.CommentRequest;
import com.lucas3.contanos.model.request.IncidentRequest;
import com.lucas3.contanos.model.response.ContameMapResponse;
import com.lucas3.contanos.model.response.geocoding.LocationResponse;
import com.lucas3.contanos.repository.*;
import com.lucas3.contanos.service.firebase.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class IncidentService implements IIncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImgbbService imgbbService;

    @Autowired
    private IGeocodingService geocodingService;

    @Autowired
    private NotificationService notificationService;

    private static final Map<EIncidentCategory, List<String>> subcategories;
    static {
        Map<EIncidentCategory, List<String>> map = new HashMap<>();
        map.put(EIncidentCategory.ALUMBRADO, Arrays.asList("Cable suelto",
                "Despeje de ramas",
                "Luminaria no funciona",
                "Luminaria prendida de día",
                "Pedido de luminaria nueva",
                "Poste en mal estado",
                "Reclamos varios"));

        map.put(EIncidentCategory.AUTOS, Arrays.asList("Autos abandonados"));

        map.put(EIncidentCategory.VIA_PUBLICA, Arrays.asList("Corte de pasto",
                "Bache",
                "Cordón a reparar",
                "Semáforo en riesgo de caída",
                "Solicitud de rampa para personas con movilidad reducida",
                "Solicitudes varias"));

        map.put(EIncidentCategory.LIMPIEZA, Arrays.asList("Destapar desagüe",
                "Falta servicio de recolección",
                "Falta servicio de barrido",
                "Solicitud de recolección",
                "Falta tapa boca de desagüe",
                "Reparar desagüe","Reclamos varios"));

        map.put(EIncidentCategory.ESPACIOS_VERDES, Arrays.asList("Corte de césped",
                "Extracción de Árbol",
                "Limpieza y vaciado de cestos de espacios públicos",
                "Solicitud de poda",
                "Retiro de poda",
                "Despeje de ramas en luminaria/semáforo",
                "Permiso de extracción a cargo del vecino"));

        map.put(EIncidentCategory.USO_ESPACIO, Arrays.asList("Carteles Publicitarios con riesgo de caída",
                "Carteles o columnas abandonadas",
                "Ocupación indebida del espacio público",
                "Puesto abandonado o falta mantenimiento",
                "Reparación de rampa para personas con movilidad reducida"));

        subcategories = Collections.unmodifiableMap(map);
    }


    @Override
    public Incident createIncident(IncidentRequest request, String email) throws FailedToLoadImageException, FailedReverseGeocodeException, UserNotFoundException {
        EIncidentCategory category = EIncidentCategory.valueOf(request.getCategory());

        Incident incident = new Incident(request.getTitle(),category,request.getDescription(), request.getLat(), request.getLon());

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
        incident.setState(EIncidentStatePublic.INGRESADO);
        incident.setStatePrivate(EIncidentStatePrivate.INSPECCION);
        incident.setSubcategory(request.getSubcategory());

        incidentRepository.save(incident);
        if(user.getFCMToken() != null){
            notificationService.sendIncidentNotification(user,incident);
        }


        return incident;
    }



    @Override
    public List<Incident> getAllIncidents(String email) throws UserNotFoundException {
        User user = verifyUser(email);
        List<Incident> incidents = incidentRepository.findAll();

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
    public Incident getIncidentById(Long id) throws IncidentNotFoundException{
        return verifyIncident(id);
    }

    @Override
    public List<String> getCategories() {
        List<String> result = new ArrayList<>();
        EIncidentCategory[] categories = EIncidentCategory.values();
        for (EIncidentCategory category:categories) {
            result.add(category.getValue());
        }
        Collections.sort(result);
        return result;
    }

    @Override
    public List<ContameMapResponse> getCategoriesMap() {
        List<ContameMapResponse> response = new ArrayList<>();
        List<ContameMapResponse> sorted = null;
        for ( EIncidentCategory cat: EIncidentCategory.values()) {
            response.add(new ContameMapResponse(cat.name(), cat.getValue()));
        }
        sorted = response.stream().sorted(Comparator.comparing(ContameMapResponse::getKey)).collect(Collectors.toList());
        return sorted;
    }

    @Override
    public List<String> getSubcategories(String category) {
        EIncidentCategory cat = EIncidentCategory.valueOf(category);
        List<String> result = subcategories.get(cat);
        Collections.sort(result);
        return result ;
    }


    @Override
    public Comment createCommentUser(CommentRequest request, Long idIncident, String email) throws UserNotFoundException, IncidentNotFoundException {
        User user = verifyUser(email);
        Incident incident = verifyIncident(idIncident);

        Comment comment = new Comment(request.getComment(),user,incident);
        comment.setCategory(ECommentCategory.PUBLIC);
        comment.setDate(new Date());
        commentRepository.save(comment);
        if(!user.getId().equals(incident.getUser().getId())){
            notificationService.sendCommentUserNotification(user,incident);
        }

        if(comment.getUser().getId().equals(incident.getUser().getId())){
            comment.setOwner(true);
        }else{
            comment.setOwner(false);
        }

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
        comment.setDate(new Date());
        commentRepository.save(comment);

        if (category.equals(ECommentCategory.PUBLIC)){
            notificationService.sendCommentAdminNotification(incident);
        }
        return comment;
    }

    @Override
    public List<Comment> getComments(Long idIncident) throws IncidentNotFoundException {
        Incident incident = verifyIncident(idIncident);

       List<Comment> comments = commentRepository.findAllByIncidentOrderByDateDesc(incident);
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
    public List<Comment> getPublicComments(Long idIncident) throws IncidentNotFoundException {
        Incident incident = verifyIncident(idIncident);

        List<Comment> comments = commentRepository.findAllByIncidentAndCategoryOrderByDateDesc(incident, ECommentCategory.PUBLIC);
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
        return commentRepository.findAllByIncidentAndCategoryOrderByDateDesc(incident, ECommentCategory.PRIVATE);
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
    public List<ContameMapResponse> getStatesPublic() {
        List<ContameMapResponse> publicStates = new ArrayList<>();
        for (EIncidentStatePublic state: EIncidentStatePublic.values()) {
            publicStates.add(new ContameMapResponse(state.name(), state.getValue()));
        }
        return publicStates;
    }

    @Override
    public List<ContameMapResponse> getStatesPrivate() {
        List<ContameMapResponse> privateStates = new ArrayList<>();
        for (EIncidentStatePrivate state: EIncidentStatePrivate.values()) {
            privateStates.add(new ContameMapResponse(state.name(), state.getValue()));
        }
        return privateStates;
    }


    @Override
    public void changeState(Long id, ChangeStateRequest request, String email) throws IncidentNotFoundException, UserNotFoundException {
        Incident incident = verifyIncident(id);

        EIncidentStatePublic newState = EIncidentStatePublic.valueOf(request.getState());

        changeStateIncident(incident,newState);

        if(request.getComment() != null && !request.getComment().isEmpty()){
            postCommentAdmin(request.getComment(),email, incident);
        }

    }


    private void changeStateIncident(Incident incident, EIncidentStatePublic state){
        if(state.equals(EIncidentStatePublic.RESUELTO) || state.equals(EIncidentStatePublic.INVALIDO)  ){
            incident.setCompleteDate(new Date());
        }
        incident.setState(state);
        incident.setUpdateDate(new Date());
        incidentRepository.save(incident);
        notificationService.sendChangeStateNotification(incident);

    }

    @Override
    public void changeStatePrivate(Long id, ChangeStateRequest request, String email) throws IncidentNotFoundException, UserNotFoundException {
        Incident incident = verifyIncident(id);

        EIncidentStatePrivate newState = EIncidentStatePrivate.valueOf(request.getState());

        changeStatePrivateIncident(incident,newState);

        if(request.getComment() != null && !request.getComment().isEmpty()){
            postCommentAdmin(request.getComment(),email, incident);
        }

    }


    private void changeStatePrivateIncident(Incident incident, EIncidentStatePrivate state){
        if(state.equals(EIncidentStatePrivate.RECHAZO_PRESUPUESTO) || state.equals(EIncidentStatePrivate.RESUELTO)  ){
            incident.setCompleteDate(new Date());
        }
        incident.setStatePrivate(state);
        incident.setUpdateDate(new Date());
        incidentRepository.save(incident);
    }

    private void postCommentAdmin(String commentary, String email, Incident incident) throws UserNotFoundException {
        User user = verifyUser(email);
        Comment comment = new Comment(commentary,user,incident);
        comment.setCategory(ECommentCategory.PRIVATE);
        comment.setDate(new Date());
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
