package com.lucas3.contanos.service;

import com.lucas3.contanos.entities.*;
import com.lucas3.contanos.model.exception.*;
import com.lucas3.contanos.model.request.ChangeStateRequest;
import com.lucas3.contanos.model.request.CommentRequest;
import com.lucas3.contanos.model.request.IncidentRequest;
import com.lucas3.contanos.model.request.RegisterRequest;
import com.lucas3.contanos.model.response.ContameMapResponse;
import org.junit.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Test del servicio de reportes
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
public class IncidentServiceTests {

    @Autowired
    private IIncidentService incidentService;

    @Autowired
    private IUserService userService;


    @Test
    public void createTest() throws IOException, FailedToLoadImageException, FailedReverseGeocodeException, EmailTakenException, InvalidPasswordException, UserNotFoundException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        IncidentRequest request = new IncidentRequest("Prueba", EIncidentCategory.ALUMBRADO.name());
        Incident incident = new Incident("Prueba", EIncidentCategory.ALUMBRADO);
        incident.setId(1L);
        Incident incident1 = incidentService.createIncident(request,"prueba@prueba.com");
        Assert.assertEquals(incident.getTitle(), incident1.getTitle());
        Assert.assertEquals(incident.getDescription(), incident1.getDescription());
        Assert.assertEquals(incident.getId(), incident1.getId());
    }


    @Test
    public void findAllTest() throws IOException, FailedToLoadImageException, FailedReverseGeocodeException, EmailTakenException, InvalidPasswordException, UserNotFoundException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        IncidentRequest request = new IncidentRequest("Prueba", EIncidentCategory.ALUMBRADO.name());
        incidentService.createIncident(request,"prueba@prueba.com");
        incidentService.createIncident(request,"prueba@prueba.com");
        incidentService.createIncident(request,"prueba@prueba.com");
        List<Incident> incidents = incidentService.getAllIncidents();
        Assert.assertTrue(incidents.size() == 3);
    }

    @Test
    public void findAllByUserTest() throws IOException, FailedToLoadImageException, FailedReverseGeocodeException, EmailTakenException, InvalidPasswordException, UserNotFoundException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        IncidentRequest request = new IncidentRequest("Prueba", EIncidentCategory.ALUMBRADO.name());
        incidentService.createIncident(request,"prueba@prueba.com");
        incidentService.createIncident(request,"prueba@prueba.com");
        incidentService.createIncident(request,"prueba@prueba.com");
        List<Incident> incidents = incidentService.getAllIncidentsByUser("prueba@prueba.com");
        Assert.assertTrue(incidents.size() == 3);
    }

    @Test
    public void findAllByUserWithoutVotesTest() throws IOException, FailedToLoadImageException, FailedReverseGeocodeException, EmailTakenException, InvalidPasswordException, UserNotFoundException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        IncidentRequest request = new IncidentRequest("Prueba", EIncidentCategory.ALUMBRADO.name());
        incidentService.createIncident(request,"prueba@prueba.com");
        incidentService.createIncident(request,"prueba@prueba.com");
        incidentService.createIncident(request,"prueba@prueba.com");
        List<Incident> incidents = incidentService.getAllIncidents("prueba@prueba.com");
        Assert.assertTrue(incidents.size() == 3);
    }


    @Test
    public void findByIdTest() throws IOException, FailedToLoadImageException, FailedReverseGeocodeException, EmailTakenException, InvalidPasswordException, UserNotFoundException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        Incident result = null;
        IncidentRequest request = new IncidentRequest("Prueba", EIncidentCategory.ALUMBRADO.name());
        IncidentRequest request3 = new IncidentRequest("Prueba3", EIncidentCategory.ALUMBRADO.name());
        incidentService.createIncident(request,"prueba@prueba.com");
        incidentService.createIncident(request,"prueba@prueba.com");
        incidentService.createIncident(request3,"prueba@prueba.com");
        try{
            result = incidentService.getIncidentById(3L,"prueba@prueba.com");
        } catch (IncidentNotFoundException | UserNotFoundException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(request3.getTitle(), result.getTitle());
        Assert.assertEquals(request3.getCategory() , result.getCategory().name());
    }

    @Test
    public void findByIdWithoutUserTest() throws IOException, FailedToLoadImageException, FailedReverseGeocodeException, EmailTakenException, InvalidPasswordException, UserNotFoundException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        Incident result = null;
        IncidentRequest request = new IncidentRequest("Prueba", EIncidentCategory.ALUMBRADO.name());
        incidentService.createIncident(request,"prueba@prueba.com");
        try{
            result = incidentService.getIncidentById(1L);
        } catch (IncidentNotFoundException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(request.getTitle(), result.getTitle());
        Assert.assertEquals(request.getCategory() , result.getCategory().name());
    }

    @Test
    public void getCategoriesTest(){
        List<String> categories = incidentService.getCategories();
        Assert.assertTrue(categories.contains("Vía Pública"));
        Assert.assertTrue(categories.contains("Limpieza"));
        Assert.assertTrue(categories.contains("Espacios verdes y arbolado"));
        Assert.assertTrue(categories.contains("Uso del espacio público"));
        Assert.assertTrue(categories.contains("Alumbrado Público"));
        Assert.assertTrue(categories.contains("Autos Abandonados"));
    }
    @Test
    public void getCategoriesMapTest(){
        List<ContameMapResponse> categories = incidentService.getCategoriesMap();
        Assert.assertEquals(6, categories.size());
        Assert.assertEquals("Alumbrado Público", categories.get(0).getValue());
        Assert.assertEquals("Vía Pública", categories.get(5).getValue());
    }

    @Test
    public void getStatePublicMapTest(){
        List<ContameMapResponse> states = incidentService.getStatesPublic();
        Assert.assertEquals(6, states.size());
        Assert.assertEquals("Ingresado", states.get(0).getValue());
        Assert.assertEquals("Pendiente de obra mayor", states.get(5).getValue());
    }

    @Test
    public void getStatePrivateMapTest(){
        List<ContameMapResponse> states = incidentService.getStatesPrivate();
        Assert.assertEquals(6, states.size());
        Assert.assertEquals("Inspección", states.get(0).getValue());
        Assert.assertEquals("Pendiente de obra integral", states.get(5).getValue());
    }

    @Test
    public void getSubcategoryTest(){
        List<String> sub = incidentService.getSubcategories(EIncidentCategory.AUTOS.name());
        Assert.assertEquals(1, sub.size());
        Assert.assertTrue(sub.contains("Autos abandonados"));

    }

    @Test
    public void voteForMyIncidentTest() throws FailedToLoadImageException, FailedReverseGeocodeException, EmailTakenException, InvalidPasswordException, UserNotFoundException, InvalidVoteException, IncidentNotFoundException {
        boolean thrown = false;
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        IncidentRequest request = new IncidentRequest("Prueba", EIncidentCategory.ALUMBRADO.name());
        Incident incident = new Incident("Prueba", EIncidentCategory.ALUMBRADO);
        incident.setId(1L);
        incidentService.createIncident(request,"prueba@prueba.com");
        try{
            incidentService.vote(1L,"prueba@prueba.com");
        }catch(InvalidVoteException e){
            thrown = true;
        }
        Assert.assertTrue(thrown);

    }

    @Test
    public void voteTest() throws FailedToLoadImageException, FailedReverseGeocodeException, EmailTakenException, InvalidPasswordException, UserNotFoundException, InvalidVoteException, IncidentNotFoundException, VoteNotFoundException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        RegisterRequest register2 = new RegisterRequest("prueba2@prueba.com", "Prueba123#");
        userService.registerUser(register2);
        IncidentRequest request = new IncidentRequest("Prueba", EIncidentCategory.ALUMBRADO.name());
        Incident incident = new Incident("Prueba", EIncidentCategory.ALUMBRADO);
        incident.setId(1L);
        incidentService.createIncident(request,"prueba@prueba.com");
        incidentService.vote(1L,"prueba2@prueba.com");
        Incident result = incidentService.getIncidentById(1L,"prueba@prueba.com");
        Assert.assertEquals(result.getVotes().intValue(), 1L);

    }

    @Test
    public void unvoteTest() throws FailedToLoadImageException, FailedReverseGeocodeException, EmailTakenException, InvalidPasswordException, UserNotFoundException, InvalidVoteException, IncidentNotFoundException, VoteNotFoundException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        RegisterRequest register2 = new RegisterRequest("prueba2@prueba.com", "Prueba123#");
        userService.registerUser(register2);
        IncidentRequest request = new IncidentRequest("Prueba", EIncidentCategory.ALUMBRADO.name());
        Incident incident = new Incident("Prueba", EIncidentCategory.ALUMBRADO);
        incident.setId(1L);
        incidentService.createIncident(request,"prueba@prueba.com");
        incidentService.vote(1L,"prueba2@prueba.com");
        incidentService.unvote(1L,"prueba2@prueba.com");
        Incident result = incidentService.getIncidentById(1L,"prueba@prueba.com");
        Assert.assertEquals(result.getVotes().intValue(), 0);

    }

    @Test
    public void unvoteWithoutVoteTest() throws FailedToLoadImageException, FailedReverseGeocodeException, EmailTakenException, InvalidPasswordException, UserNotFoundException, IncidentNotFoundException {
        boolean thrown = false;
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        RegisterRequest register2 = new RegisterRequest("prueba2@prueba.com", "Prueba123#");
        userService.registerUser(register2);
        IncidentRequest request = new IncidentRequest("Prueba", EIncidentCategory.ALUMBRADO.name());
        Incident incident = new Incident("Prueba", EIncidentCategory.ALUMBRADO);
        incident.setId(1L);
        incidentService.createIncident(request,"prueba@prueba.com");
        try{
            incidentService.unvote(1L,"prueba2@prueba.com");
        }catch(VoteNotFoundException e){
            thrown = true;
        }
        Assert.assertTrue(thrown);
    }

    @Test
    public void getHood(){
        String location = "Amphitheatre Py 1600, Charleston Terrace";
        String result = location.split(",")[1].trim();

        Assert.assertEquals(result,"Charleston Terrace");
    }

    @Test
    public void changeStateTest() throws FailedToLoadImageException, FailedReverseGeocodeException, IncidentNotFoundException, EmailTakenException, InvalidPasswordException, StateNotFoundException, UserNotFoundException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        Incident result = null;
        IncidentRequest request = new IncidentRequest("Prueba", EIncidentCategory.ALUMBRADO.name());
        incidentService.createIncident(request,"prueba@prueba.com");
        ChangeStateRequest changeRequest = new ChangeStateRequest(EIncidentStatePublic.INVALIDO.name(), "Hola esto es un comentario");
        incidentService.changeState(1L,changeRequest,"prueba@prueba.com");
        try{
            result = incidentService.getIncidentById(1L,"prueba@prueba.com");
        } catch (IncidentNotFoundException | UserNotFoundException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(result.getState(), EIncidentStatePublic.INVALIDO);
    }

    @Test
    public void changeStatePrivateTest() throws FailedToLoadImageException, FailedReverseGeocodeException, IncidentNotFoundException, EmailTakenException, InvalidPasswordException, StateNotFoundException, UserNotFoundException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        Incident result = null;
        IncidentRequest request = new IncidentRequest("Prueba", EIncidentCategory.ALUMBRADO.name());
        incidentService.createIncident(request,"prueba@prueba.com");
        ChangeStateRequest changeRequest = new ChangeStateRequest(EIncidentStatePrivate.RECHAZO_PRESUPUESTO.name(), "Hola esto es un comentario");
        incidentService.changeStatePrivate(1L,changeRequest,"prueba@prueba.com");
        try{
            result = incidentService.getIncidentById(1L,"prueba@prueba.com");
        } catch (IncidentNotFoundException | UserNotFoundException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(result.getStatePrivate(), EIncidentStatePrivate.RECHAZO_PRESUPUESTO);
    }

    @Test
    public void changeStatePrivateWrongTest() throws FailedToLoadImageException, FailedReverseGeocodeException, IncidentNotFoundException, EmailTakenException, InvalidPasswordException, StateNotFoundException, UserNotFoundException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        boolean thrown = false;
        IncidentRequest request = new IncidentRequest("Prueba", EIncidentCategory.ALUMBRADO.name());
        incidentService.createIncident(request,"prueba@prueba.com");
        ChangeStateRequest changeRequest = new ChangeStateRequest(EIncidentStatePublic.INVALIDO.name(), "Hola esto es un comentario");
        try{
            incidentService.changeStatePrivate(1L,changeRequest,"prueba@prueba.com");
        }catch(IllegalArgumentException e){
            thrown = true;
        }
        Assert.assertTrue(thrown);
    }

    @Test
    public void changeStatePublicWrongTest() throws FailedToLoadImageException, FailedReverseGeocodeException, IncidentNotFoundException, EmailTakenException, InvalidPasswordException, StateNotFoundException, UserNotFoundException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        boolean thrown = false;
        IncidentRequest request = new IncidentRequest("Prueba", EIncidentCategory.ALUMBRADO.name());
        incidentService.createIncident(request,"prueba@prueba.com");
        ChangeStateRequest changeRequest = new ChangeStateRequest(EIncidentStatePrivate.RECHAZO_PRESUPUESTO.name(), "Hola esto es un comentario");
        try{
            incidentService.changeState(1L,changeRequest,"prueba@prueba.com");
        }catch(IllegalArgumentException e){
            thrown = true;
        }
        Assert.assertTrue(thrown);
    }

    @Test
    public void createCommentTest() throws FailedToLoadImageException, FailedReverseGeocodeException, EmailTakenException, InvalidPasswordException, UserNotFoundException, IncidentNotFoundException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        IncidentRequest request = new IncidentRequest("Prueba", EIncidentCategory.ALUMBRADO.name());
        Incident incident = new Incident("Prueba", EIncidentCategory.ALUMBRADO);
        incident.setId(1L);
        incidentService.createIncident(request,"prueba@prueba.com");
        CommentRequest requestComment = new CommentRequest();
        requestComment.setCategory(ECommentCategory.PUBLIC.name());
        requestComment.setComment("Comentario de prueba");
        incidentService.createCommentUser(requestComment,1L,"prueba@prueba.com");

        Incident inc = incidentService.getIncidentById(1L,"prueba@prueba.com");
        Assert.assertEquals(1, inc.getComments().size());
        Assert.assertEquals("Comentario de prueba", inc.getComments().get(0).getComment());

    }

    @Test
    public void createCommentAdminTest() throws FailedToLoadImageException, FailedReverseGeocodeException, EmailTakenException, InvalidPasswordException, UserNotFoundException, IncidentNotFoundException, InvalidCategoryException, CategoryNotFoundException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        IncidentRequest request = new IncidentRequest("Prueba", EIncidentCategory.ALUMBRADO.name());
        Incident incident = new Incident("Prueba", EIncidentCategory.ALUMBRADO);
        incident.setId(1L);
        incidentService.createIncident(request,"prueba@prueba.com");
        CommentRequest requestComment = new CommentRequest();
        requestComment.setCategory(ECommentCategory.PRIVATE.name());
        requestComment.setComment("Comentario de prueba");
        incidentService.createCommentAdmin(requestComment,1L,"prueba@prueba.com");

        List<Comment> comments = incidentService.getPrivateComments(1L);
        Assert.assertEquals(1, comments.size());
        Assert.assertEquals("Comentario de prueba", comments.get(0).getComment());
        Assert.assertEquals(ECommentCategory.PRIVATE, comments.get(0).getCategory());

    }

    @Test
    public void createCommentAnotherTest() throws FailedToLoadImageException, FailedReverseGeocodeException, EmailTakenException, InvalidPasswordException, UserNotFoundException, IncidentNotFoundException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        RegisterRequest register2 = new RegisterRequest("prueba2@prueba.com", "Prueba123#");
        userService.registerUser(register2);
        IncidentRequest request = new IncidentRequest("Prueba", EIncidentCategory.ALUMBRADO.name());
        Incident incident = new Incident("Prueba", EIncidentCategory.ALUMBRADO);
        incident.setId(1L);
        incidentService.createIncident(request,"prueba@prueba.com");
        CommentRequest requestComment = new CommentRequest();
        requestComment.setCategory(ECommentCategory.PUBLIC.name());
        requestComment.setComment("Comentario de prueba");
        incidentService.createCommentUser(requestComment,1L,"prueba2@prueba.com");

        Incident inc = incidentService.getIncidentById(1L,"prueba@prueba.com");
        Assert.assertEquals(1, inc.getComments().size());
        Assert.assertEquals("Comentario de prueba", inc.getComments().get(0).getComment());
        Assert.assertEquals(false, inc.getComments().get(0).isOwner());

    }

    @Test
    public void getCommentsTest() throws FailedToLoadImageException, FailedReverseGeocodeException, EmailTakenException, InvalidPasswordException, UserNotFoundException, IncidentNotFoundException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        RegisterRequest register2 = new RegisterRequest("prueba2@prueba.com", "Prueba123#");
        userService.registerUser(register2);
        IncidentRequest request = new IncidentRequest("Prueba", EIncidentCategory.ALUMBRADO.name());
        Incident incident = new Incident("Prueba", EIncidentCategory.ALUMBRADO);
        incident.setId(1L);
        incidentService.createIncident(request,"prueba@prueba.com");
        CommentRequest requestComment = new CommentRequest();
        requestComment.setCategory(ECommentCategory.PUBLIC.name());
        requestComment.setComment("Comentario de prueba");
        incidentService.createCommentUser(requestComment,1L,"prueba2@prueba.com");
        incidentService.createCommentUser(requestComment,1L,"prueba@prueba.com");

        List<Comment> comments = incidentService.getComments(1L);
        Assert.assertEquals(2, comments.size());
    }

    @Test
    public void getPrivateCommentsTest() throws FailedToLoadImageException, FailedReverseGeocodeException, EmailTakenException, InvalidPasswordException, UserNotFoundException, IncidentNotFoundException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        RegisterRequest register2 = new RegisterRequest("prueba2@prueba.com", "Prueba123#");
        userService.registerUser(register2);
        IncidentRequest request = new IncidentRequest("Prueba", EIncidentCategory.ALUMBRADO.name());
        Incident incident = new Incident("Prueba", EIncidentCategory.ALUMBRADO);
        incident.setId(1L);
        incidentService.createIncident(request,"prueba@prueba.com");
        CommentRequest requestComment = new CommentRequest();
        requestComment.setCategory(ECommentCategory.PUBLIC.name());
        requestComment.setComment("Comentario de prueba");
        incidentService.createCommentUser(requestComment,1L,"prueba2@prueba.com");
        incidentService.createCommentUser(requestComment,1L,"prueba@prueba.com");

        List<Comment> comments = incidentService.getPrivateComments(1L);
        Assert.assertEquals(0, comments.size());
    }



    @Test
    public void setFatherTest() throws FailedToLoadImageException, FailedReverseGeocodeException, EmailTakenException, InvalidPasswordException, UserNotFoundException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        Incident son = null;

        IncidentRequest request = new IncidentRequest("Prueba", EIncidentCategory.ALUMBRADO.name());
        IncidentRequest request2 = new IncidentRequest("Prueba2", EIncidentCategory.ALUMBRADO.name());
        incidentService.createIncident(request,"prueba@prueba.com");
        incidentService.createIncident(request2,"prueba@prueba.com");

        try{
            incidentService.setFather(1L,2L);
            son = incidentService.getIncidentById(1L,"prueba@prueba.com");
        } catch (Exception e){
            e.printStackTrace();
        }
        Assert.assertNotNull(son.getParent());
        Assert.assertTrue(son.getParent().getId() == 2L);
    }

    @Test
    public void getSonTest() throws FailedToLoadImageException, FailedReverseGeocodeException, EmailTakenException, InvalidPasswordException, UserNotFoundException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        Incident son = null;
        List<Incident> sons = new ArrayList<>();

        IncidentRequest request = new IncidentRequest("Prueba", EIncidentCategory.ALUMBRADO.name());
        IncidentRequest request2 = new IncidentRequest("Prueba2", EIncidentCategory.ALUMBRADO.name());
        incidentService.createIncident(request,"prueba@prueba.com");
        incidentService.createIncident(request2,"prueba@prueba.com");

        try{
            incidentService.setFather(1L,2L);
            sons = incidentService.getSons(2L);
        } catch (Exception e){
            e.printStackTrace();
        }
        Assert.assertNotNull(sons);
        Assert.assertTrue(sons.size() == 1);
        Assert.assertTrue(1L == sons.get(0).getId());
    }



}
