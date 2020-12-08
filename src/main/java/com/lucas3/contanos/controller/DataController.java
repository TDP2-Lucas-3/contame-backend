package com.lucas3.contanos.controller;

import com.lucas3.contanos.entities.Incident;
import com.lucas3.contanos.model.exception.InvalidTokenException;
import com.lucas3.contanos.model.filters.DataFilter;
import com.lucas3.contanos.model.request.DataLoadRequest;
import com.lucas3.contanos.model.response.IncidentResponse;
import com.lucas3.contanos.model.response.MapDataResponse;
import com.lucas3.contanos.model.response.StandResponse;
import com.lucas3.contanos.service.IDataService;
import io.swagger.annotations.Api;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@Api(value = "Api de para la carga de la aplicacion", description = "API para la carga de datos de la aplicacion")
@RequestMapping("/data")
public class DataController {

    @Autowired
    private IDataService dataService;

    @Value("${contame.app.secretAdmin}")
    private String secretAdmin;

    @PostMapping(value= "/load")
    public ResponseEntity<?> loadData( @RequestHeader("Authorization") String token){
        try{
            validateToken(token);
            dataService.loadData();
            return ResponseEntity.ok().build();
        } catch (InvalidTokenException e) {
            return ResponseEntity.badRequest().body(new StandResponse("CREDENCIALES INVALIDAS"));
        }

    }

    @PostMapping(value= "/load/custom")
    public ResponseEntity<?> loadDataCustom(@RequestHeader("Authorization") String token, @RequestBody DataLoadRequest request){
        try{
            validateToken(token);
            dataService.loadDataCustom(request);
            return ResponseEntity.ok().build();
        } catch (InvalidTokenException e) {
            return ResponseEntity.badRequest().body(new StandResponse("CREDENCIALES INVALIDAS"));
        }

    }

    @PostMapping(value= "/state")
    public ResponseEntity<?> stateData(@RequestBody(required = false) DataFilter filter){
        try {
            if(filter != null){
                return ResponseEntity.ok(dataService.getStatesData(filter));
            }else{
                return ResponseEntity.ok(dataService.getStatesData(new DataFilter()));
            }

        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(new StandResponse("Fecha en formano incorrecto " + filter.getFrom()));
        }
    }

    @GetMapping(value= "/map")
    public ResponseEntity<?> mapData(@RequestParam(required = false) String category){
        MapDataResponse response = new MapDataResponse();
        List<IncidentResponse> incidents = new ArrayList<>();
        for (Incident incident: dataService.getIncidents(category)) {
            incidents.add(new IncidentResponse(incident));
        }
        response.setIncidents(incidents);
        response.setHoodRanking(dataService.getHoodRanking(category));

        return ResponseEntity.ok(response);
    }

    private void validateToken(String token) throws InvalidTokenException {
        if (token.startsWith("Contame ")) {
            String secret = DigestUtils.sha256Hex(token.substring(8, token.length()));
            if(!secret.equals(secretAdmin)) throw new InvalidTokenException();
        }else{
            throw new InvalidTokenException();
        }
    }
}
