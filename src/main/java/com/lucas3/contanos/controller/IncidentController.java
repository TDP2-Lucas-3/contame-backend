package com.lucas3.contanos.controller;

import com.lucas3.contanos.entities.Category;
import com.lucas3.contanos.entities.Incident;
import com.lucas3.contanos.model.exception.InvalidCategoryException;
import com.lucas3.contanos.model.exception.InvalidIncidentException;
import com.lucas3.contanos.model.request.CategoryRequest;
import com.lucas3.contanos.model.request.IncidentRequest;
import com.lucas3.contanos.model.response.StandResponse;
import com.lucas3.contanos.security.jwt.JwtUtils;
import com.lucas3.contanos.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@RequestMapping("/incident")
public class IncidentController {

    @Autowired
    private IncidentService incidentService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping(value= "")
    public ResponseEntity<?> createIncident(@RequestHeader("Authorization") String fullToken,@RequestBody IncidentRequest request) {
        Incident response = null;
        try{
            validateIncident(request);
            String email = jwtUtils.getUserEmailFromJwtToken(fullToken);
            response = incidentService.createIncident(request, email);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity
                    .badRequest()
                    .body(new StandResponse("Hubo un error creando tu incidente, Por favor intenta devuelta en unos minutos"));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    public List<Incident> getIncidents(){
       return incidentService.getAllIncidents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?>  getIncidentById(@PathVariable Long id)  {
        try{
            return ResponseEntity.ok(incidentService.getIncidentById(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new StandResponse("El incidente solicitado no existe"));
        }
    }
    @GetMapping("/categories")
    public List<Category> getCategories(){
        return incidentService.getCategories();
    }

    @PostMapping("/categories")
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest categoryRequest){
        Category category = null;
        try{
            validateCategory(categoryRequest);
            category = incidentService.createCategory(categoryRequest);
            return ResponseEntity.ok(category);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(new StandResponse("No se pudo crear la categoria"));
        }
    }

    private void validateCategory(CategoryRequest request) throws InvalidCategoryException {
        if(request.getName() == null || StringUtils.isEmpty(request.getName())) throw new InvalidCategoryException();
        if(request.getDescription() == null || StringUtils.isEmpty(request.getDescription())) throw new InvalidCategoryException();
    }

    private void validateIncident(IncidentRequest request) throws InvalidIncidentException {
        if(request.getTitle() == null || StringUtils.isEmpty(request.getTitle())) throw new InvalidIncidentException();
        if(request.getCategory() == null || StringUtils.isEmpty(request.getCategory())) throw new InvalidIncidentException();
    }




}
