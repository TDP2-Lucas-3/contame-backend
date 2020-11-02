package com.lucas3.contanos.service;

import com.lucas3.contanos.entities.Category;
import com.lucas3.contanos.entities.Incident;
import com.lucas3.contanos.model.exception.FailedReverseGeocodeException;
import com.lucas3.contanos.model.exception.FailedToLoadImageException;
import com.lucas3.contanos.model.request.CategoryRequest;
import com.lucas3.contanos.model.request.IncidentRequest;
import com.lucas3.contanos.model.exception.IncidentNotFoundException;
import com.lucas3.contanos.model.request.RegisterRequest;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
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
    public void createTest() throws IOException, FailedToLoadImageException, FailedReverseGeocodeException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "prueba123");
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
    public void findAllTest() throws IOException, FailedToLoadImageException, FailedReverseGeocodeException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "prueba123");
        userService.registerUser(register);
        Category category = incidentService.createCategory(new CategoryRequest("ROBO", "Incidencia de robo"));
        IncidentRequest request = new IncidentRequest("Prueba", 1L);
        Incident incident1 = incidentService.createIncident(request,"prueba@prueba.com");
        Incident incident2 = incidentService.createIncident(request,"prueba@prueba.com");
        Incident incident3 = incidentService.createIncident(request,"prueba@prueba.com");
        List<Incident> incidents = incidentService.getAllIncidents();
        Assert.assertTrue(incidents.size() == 3);
    }

    @Test
    public void findByIdTest() throws IOException, FailedToLoadImageException, FailedReverseGeocodeException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "prueba123");
        userService.registerUser(register);
        Category category = incidentService.createCategory(new CategoryRequest("ROBO", "Incidnecia de robo"));
        Incident result = null;
        IncidentRequest request = new IncidentRequest("Prueba", 1L);
        IncidentRequest request3 = new IncidentRequest("Prueba3", 1L);
        Incident incident1 = incidentService.createIncident(request,"prueba@prueba.com");
        Incident incident2 = incidentService.createIncident(request,"prueba@prueba.com");
        Incident incident3 = incidentService.createIncident(request3,"prueba@prueba.com");
        try{
            result = incidentService.getIncidentById(3L);
        } catch (IncidentNotFoundException e) {
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
            Category category1 = incidentService.createCategory(new CategoryRequest("Robo", "Incidencia de robo"));
            Category category2 = incidentService.createCategory(new CategoryRequest("Robo", "Incidencia de robo"));
        }catch(Exception e){

        }
        List<Category> categories = incidentService.getCategories();
        Assert.assertEquals(categories.size(), 1);
    }

}
