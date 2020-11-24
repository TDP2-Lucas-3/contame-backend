package com.lucas3.contanos.controller;

import com.lucas3.contanos.entities.EIncidentCategory;
import com.lucas3.contanos.entities.Incident;
import com.lucas3.contanos.model.exception.*;
import com.lucas3.contanos.model.filters.IncidentFilter;
import com.lucas3.contanos.model.request.ChangeStateRequest;
import com.lucas3.contanos.model.request.CommentRequest;
import com.lucas3.contanos.model.request.IncidentRequest;
import com.lucas3.contanos.model.response.ContameMapResponse;
import com.lucas3.contanos.model.response.IncidentResponse;
import com.lucas3.contanos.model.response.StandResponse;
import com.lucas3.contanos.security.jwt.JwtUtils;
import com.lucas3.contanos.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        IncidentResponse response = null;
        try{
            validateIncident(request);
            String email = jwtUtils.getUserEmailFromJwtToken(fullToken);
            response = new IncidentResponse(incidentService.createIncident(request, email));

        } catch (FailedReverseGeocodeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new StandResponse("Error generando el geocoding"));
        } catch (FailedToLoadImageException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new StandResponse("Error subiendo la imagen del incidente"));
        } catch (InvalidIncidentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new StandResponse("La cateogria y el titulo no pueden ser nulos"));
        }catch(Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(new StandResponse("Hubo un error creando tu incidente, Por favor intenta devuelta en unos minutos"));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    public ResponseEntity<?> getIncidents(@RequestHeader("Authorization") String fullToken){

        List<IncidentResponse> response = new ArrayList<>();
        try {
            String email = jwtUtils.getUserEmailFromJwtToken(fullToken);
            for (Incident incident: incidentService.getAllIncidents(email)) {
                response.add(new IncidentResponse(incident));
            }
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new StandResponse("Hubo un error creando tu incidente, Por favor intenta devuelta en unos minutos"));
        }
    }

    @GetMapping("/self")
    public ResponseEntity<?> getMyIncidents(@RequestHeader("Authorization") String fullToken){
        List<IncidentResponse> response = new ArrayList<>();
        try{
            String email = jwtUtils.getUserEmailFromJwtToken(fullToken);
            for (Incident incident: incidentService.getAllIncidentsByUser(email)) {
                response.add(new IncidentResponse(incident));
            }
            return ResponseEntity.ok(response);
        }catch (UserNotFoundException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new StandResponse("Hubo un error creando tu incidente, Por favor intenta devuelta en unos minutos"));
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?>  getIncidentById(@PathVariable Long id,@RequestHeader("Authorization") String fullToken)  {
        try{
            String email = jwtUtils.getUserEmailFromJwtToken(fullToken);
            return ResponseEntity.ok(new IncidentResponse(incidentService.getIncidentById(id, email)));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El usuario no existe - TOKEN INVALIDO"));
        } catch (IncidentNotFoundException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El inicdente no existe - ID INVALIDO"));
        }
    }

    @PostMapping("/{id}/comment/user")
    public ResponseEntity<?>  createCommentMobile(@PathVariable Long id,@RequestHeader("Authorization") String fullToken, @RequestBody CommentRequest request)  {
        try{
            String email = jwtUtils.getUserEmailFromJwtToken(fullToken);
            validateComment(request);
            return ResponseEntity.ok(incidentService.createCommentUser(request,id,email));
        } catch (InvalidCommentException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El comentario esta vacio"));
        } catch (IncidentNotFoundException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El incidente solicitado no existe"));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El usuario no existe"));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new StandResponse("Error creando el comentario"));
        }
    }

    @PostMapping("/{id}/comment/admin")
    public ResponseEntity<?>  createCommentBackoffice(@PathVariable Long id,@RequestHeader("Authorization") String fullToken, @RequestBody CommentRequest request)  {
        try{
            String email = jwtUtils.getUserEmailFromJwtToken(fullToken);
            validateComment(request);
            return ResponseEntity.ok(incidentService.createCommentAdmin(request,id,email));
        } catch (InvalidCommentException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El comentario esta vacio"));
        } catch (IncidentNotFoundException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El incidente solicitado no existe"));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El usuario no existe"));
        } catch (InvalidCategoryException e) {
            return ResponseEntity.badRequest().body(new StandResponse("La categoria debe ser PRIVATE o PUBLIC, enviaste: " + request.getCategory()));
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.badRequest().body(new StandResponse("Los comentarios deben tener una categoria. category : PUBLIC/PRIVATE"));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new StandResponse("Error creando el comentario"));
        }
    }


    @GetMapping("/{id}/comment")
    public ResponseEntity<?>  getComments(@PathVariable Long id)  {
        try{
            return ResponseEntity.ok(incidentService.getComments(id));
        } catch (IncidentNotFoundException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El incidente solicitado no existe"));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new StandResponse("Error creando el comentario"));
        }
    }

    @GetMapping("/{id}/comment/public")
    public ResponseEntity<?>  getPublicComment(@PathVariable Long id)  {
        try{
            return ResponseEntity.ok(incidentService.getPublicComments(id));
        } catch (IncidentNotFoundException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El incidente solicitado no existe"));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new StandResponse("Error creando el comentario"));
        }
    }

    @GetMapping("/{id}/comment/private")
    public ResponseEntity<?>  getPrivateComments(@PathVariable Long id)  {
        try{
            return ResponseEntity.ok(incidentService.getPrivateComments(id));
        } catch (IncidentNotFoundException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El incidente solicitado no existe"));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new StandResponse("Error creando el comentario"));
        }
    }

    @PostMapping("/{id}/vote")
    public ResponseEntity<?>  vote(@PathVariable Long id,@RequestHeader("Authorization") String fullToken)  {
        try{
            String email = jwtUtils.getUserEmailFromJwtToken(fullToken);
            return ResponseEntity.ok(incidentService.vote(id,email));
        } catch (IncidentNotFoundException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El incidente solicitado no existe"));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El usuario no existe"));
        } catch (InvalidVoteException e) {
            return ResponseEntity.badRequest().body(new StandResponse("No podes votar por tu propia incidencia."));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new StandResponse("Error creando el voto"));
        }
    }

    @DeleteMapping("/{id}/unvote")
    public ResponseEntity<?>  unvote(@PathVariable Long id,@RequestHeader("Authorization") String fullToken)  {
        try{
            String email = jwtUtils.getUserEmailFromJwtToken(fullToken);
            incidentService.unvote(id,email);
            return ResponseEntity.ok("");
        } catch (IncidentNotFoundException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El incidente solicitado no existe"));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El usuario no existe"));
        } catch (VoteNotFoundException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El usuario habia votado por este incidente"));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new StandResponse("Error eliminando el voto"));
        }
    }

    @GetMapping("/category")
    public List<ContameMapResponse> getCategoriesEnum(){
        return incidentService.getCategoriesMap();
    }

    @GetMapping("/subcategory/{category}")
    public List<String> getSubcategories(@PathVariable String category){
        return incidentService.getSubcategories(category);
    }


    @GetMapping("/state")
    public List<ContameMapResponse> getStates(){
        return incidentService.getStatesPublic();
    }

    @GetMapping("/state/private")
    public List<ContameMapResponse> getStatesPrivate(){
        return incidentService.getStatesPrivate();
    }


    @PostMapping("/{id}/state")
    public ResponseEntity<?>  changeStates(@RequestHeader("Authorization") String fullToken,@PathVariable Long id, @RequestBody ChangeStateRequest request){
        try{
            String email = jwtUtils.getUserEmailFromJwtToken(fullToken);
            incidentService.changeState(id,request, email);
            return ResponseEntity.ok("El incidente se actualizo correctamente");
        } catch (IncidentNotFoundException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El incidente no existe - ID INVALIDO"));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(new StandResponse("Estado invalido"));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El usuario administrador existe"));
        }
    }

    @PostMapping("/{id}/state/private")
    public ResponseEntity<?>  changeStatesPrivate(@RequestHeader("Authorization") String fullToken,@PathVariable Long id, @RequestBody ChangeStateRequest request){
        try{
            String email = jwtUtils.getUserEmailFromJwtToken(fullToken);
            incidentService.changeStatePrivate(id,request, email);
            return ResponseEntity.ok("El incidente se actualizo correctamente");
        } catch (IncidentNotFoundException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El incidente no existe - ID INVALIDO"));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(new StandResponse("Estado invalido"));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El usuario administrador existe"));
        }
    }

    @PostMapping("/{idSon}/parent/{idParent}")
    public ResponseEntity<?> setFather(@PathVariable Long idSon, @PathVariable Long idParent){
        try{
            incidentService.setFather(idSon,idParent);
        } catch (IncidentSonNotFoundException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El incidente hijo no existe ID: " + idSon));
        } catch (SonHaveSonsException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El incidente hijo tiene hijos ID: " + idSon));
        } catch (IncidentFatherNotFoundException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El incidente padre no existe ID: " + idParent));
        }
        return ResponseEntity.ok("Los incidentes fueron agrupados correctamente");
    }

    @GetMapping("/{id}/parent")
    public ResponseEntity<?> getFather(@PathVariable Long id){
        try{
            return ResponseEntity.ok(new IncidentResponse(incidentService.getFather(id)));
        } catch (IncidentSonNotFoundException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El incidente no existe ID: " + id));
        } catch (IncidentFatherNotFoundException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El incidente no tiene padre"));
        }
    }

    @GetMapping("/{id}/son")
    public ResponseEntity<?> getSons(@PathVariable Long id){
        List<IncidentResponse> response = new ArrayList<>();
        try{
            for (Incident incident:incidentService.getSons(id)) {
                response.add(new IncidentResponse(incident));
            }
            return ResponseEntity.ok(response);
        } catch (IncidentSonNotFoundException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El incidente no tiene hijos"));
        } catch (IncidentFatherNotFoundException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El incidente no existe ID: " + id));
        }
    }

    private void validateIncident(IncidentRequest request) throws InvalidIncidentException {
        if(request.getTitle() == null || StringUtils.isEmpty(request.getTitle())) throw new InvalidIncidentException();
        if(request.getCategory() == null || StringUtils.isEmpty(request.getCategory())) throw new InvalidIncidentException();
    }

    private void validateComment(CommentRequest request) throws InvalidCommentException {
        if(request.getComment() == null || StringUtils.isEmpty(request.getComment())) throw new InvalidCommentException();

    }


}
