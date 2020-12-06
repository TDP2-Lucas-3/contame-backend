package com.lucas3.contanos.controller;

import com.lucas3.contanos.model.exception.InvalidTokenException;
import com.lucas3.contanos.model.filters.DataFilter;
import com.lucas3.contanos.model.request.IncidentRequest;
import com.lucas3.contanos.model.response.StandResponse;
import com.lucas3.contanos.security.jwt.JwtUtils;
import com.lucas3.contanos.service.IDataService;
import io.swagger.annotations.Api;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.POST})
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

    @PostMapping(value= "/state")
    public ResponseEntity<?> stateData(@RequestBody DataFilter filter){
        try {
            return ResponseEntity.ok(dataService.getStatesData(filter));
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(new StandResponse("Fecha en formano incorrecto " + filter.getCompleteDate()));
        }
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
