package com.lucas3.contanos.service;

import com.lucas3.contanos.entities.Category;
import com.lucas3.contanos.entities.EIncidentState;
import com.lucas3.contanos.entities.Incident;
import com.lucas3.contanos.model.exception.*;
import com.lucas3.contanos.model.filters.IncidentFilter;
import com.lucas3.contanos.model.request.CategoryRequest;
import com.lucas3.contanos.model.request.ChangeStateRequest;
import com.lucas3.contanos.model.request.IncidentRequest;
import com.lucas3.contanos.model.request.RegisterRequest;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Test del servicio de reportes
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class IncidentServiceTests {

    @Autowired
    private IncidentService incidentService;

    @Autowired
    private UserService userService;


    @Test
    public void createTest() throws IOException, FailedToLoadImageException, FailedReverseGeocodeException, EmailTakenException, InvalidPasswordException, UserNotFoundException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        Category category = incidentService.createCategory(new CategoryRequest("ROBO", "Incidnecia de robo"));
        IncidentRequest request = new IncidentRequest("Prueba", 1L);
        Incident incident = new Incident("Prueba", category);
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
        incidentService.createCategory(new CategoryRequest("ROBO", "Incidencia de robo"));
        IncidentRequest request = new IncidentRequest("Prueba", 1L);
        incidentService.createIncident(request,"prueba@prueba.com");
        incidentService.createIncident(request,"prueba@prueba.com");
        incidentService.createIncident(request,"prueba@prueba.com");
        List<Incident> incidents = incidentService.getAllIncidents();
        Assert.assertTrue(incidents.size() == 3);
    }

    @Test
    public void findAllTestEmptyFilters() throws IOException, FailedToLoadImageException, FailedReverseGeocodeException, UserNotFoundException, EmailTakenException, InvalidPasswordException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        incidentService.createCategory(new CategoryRequest("ROBO", "Incidencia de robo"));
        IncidentRequest request = new IncidentRequest("Prueba", 1L);
        IncidentFilter filter = new IncidentFilter("","");
        incidentService.createIncident(request,"prueba@prueba.com");
        incidentService.createIncident(request,"prueba@prueba.com");
        incidentService.createIncident(request,"prueba@prueba.com");
        List<Incident> incidents = incidentService.getAllIncidents("prueba@prueba.com",filter);
        Assert.assertTrue(incidents.size() == 3);
    }

    @Test
    public void findAllTestCategory() throws IOException, FailedToLoadImageException, FailedReverseGeocodeException, UserNotFoundException, EmailTakenException, InvalidPasswordException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        incidentService.createCategory(new CategoryRequest("ROBO", "Incidencia de robo"));
        incidentService.createCategory(new CategoryRequest("ROBO2", "Incidencia de robo2"));
        IncidentRequest request = new IncidentRequest("Prueba", 1L);
        IncidentFilter filter = new IncidentFilter("","ROBO");
        incidentService.createIncident(request,"prueba@prueba.com");
        incidentService.createIncident(request,"prueba@prueba.com");
        incidentService.createIncident(request,"prueba@prueba.com");
        List<Incident> incidents = incidentService.getAllIncidents("prueba@prueba.com",filter);
        Assert.assertTrue(incidents.size() == 3);
    }

    @Test
    public void findNoneTestCategory() throws IOException, FailedToLoadImageException, FailedReverseGeocodeException, UserNotFoundException, EmailTakenException, InvalidPasswordException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        incidentService.createCategory(new CategoryRequest("ROBO", "Incidencia de robo"));
        incidentService.createCategory(new CategoryRequest("ROBO2", "Incidencia de robo2"));
        IncidentRequest request = new IncidentRequest("Prueba", 1L);
        IncidentFilter filter = new IncidentFilter("","ROBO2");
        incidentService.createIncident(request,"prueba@prueba.com");
        incidentService.createIncident(request,"prueba@prueba.com");
        incidentService.createIncident(request,"prueba@prueba.com");
        List<Incident> incidents = incidentService.getAllIncidents("prueba@prueba.com",filter);
        Assert.assertTrue(incidents.size() == 0);
    }

    @Test
    public void findByIdTest() throws IOException, FailedToLoadImageException, FailedReverseGeocodeException, EmailTakenException, InvalidPasswordException, UserNotFoundException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        incidentService.createCategory(new CategoryRequest("ROBO", "Incidnecia de robo"));
        Incident result = null;
        IncidentRequest request = new IncidentRequest("Prueba", 1L);
        IncidentRequest request3 = new IncidentRequest("Prueba3", 1L);
        incidentService.createIncident(request,"prueba@prueba.com");
        incidentService.createIncident(request,"prueba@prueba.com");
        incidentService.createIncident(request3,"prueba@prueba.com");
        try{
            result = incidentService.getIncidentById(3L,"prueba@prueba.com");
        } catch (IncidentNotFoundException | UserNotFoundException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(request3.getTitle(), result.getTitle());
        Assert.assertEquals(request3.getCategory() , result.getCategory().getId());
    }

    @Test
    public void createCategory(){
        Category category = incidentService.createCategory(new CategoryRequest("Robo", "Incidencia de robo"));
        Assert.assertEquals(category.getName(),"ROBO");
        Assert.assertEquals(category.getDescription(),"Incidencia de robo");
    }

    @Test
    public void createCategoryRepeat(){
        try{
            incidentService.createCategory(new CategoryRequest("Robo", "Incidencia de robo"));
            incidentService.createCategory(new CategoryRequest("Robo", "Incidencia de robo"));
        }catch(Exception e){

        }
        List<Category> categories = incidentService.getCategories();
        Assert.assertEquals(categories.size(), 1);
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
        incidentService.createCategory(new CategoryRequest("ROBO", "Incidnecia de robo"));
        Incident result = null;
        IncidentRequest request = new IncidentRequest("Prueba", 1L);
        incidentService.createIncident(request,"prueba@prueba.com");
        ChangeStateRequest changeRequest = new ChangeStateRequest(EIncidentState.ARCHIVADO.toString(), "Hola esto es un comentario");
        incidentService.changeState(1L,changeRequest,"prueba@prueba.com");
        try{
            result = incidentService.getIncidentById(1L,"prueba@prueba.com");
        } catch (IncidentNotFoundException | UserNotFoundException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(result.getState(), EIncidentState.ARCHIVADO);
    }

    @Test
    public void setFatherTest() throws FailedToLoadImageException, FailedReverseGeocodeException, EmailTakenException, InvalidPasswordException, UserNotFoundException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        incidentService.createCategory(new CategoryRequest("ROBO", "Incidnecia de robo"));
        Incident son = null;

        IncidentRequest request = new IncidentRequest("Prueba", 1L);
        IncidentRequest request2 = new IncidentRequest("Prueba2", 1L);
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
    public void getSonTest() throws IOException, FailedToLoadImageException, FailedReverseGeocodeException, EmailTakenException, InvalidPasswordException, UserNotFoundException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "Prueba123#");
        userService.registerUser(register);
        incidentService.createCategory(new CategoryRequest("ROBO", "Incidnecia de robo"));
        Incident son = null;
        List<Incident> sons = new ArrayList<>();

        IncidentRequest request = new IncidentRequest("Prueba", 1L);
        IncidentRequest request2 = new IncidentRequest("Prueba2", 1L);
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
