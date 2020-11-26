package com.lucas3.contanos.controller;

import com.lucas3.contanos.model.exception.*;
import com.lucas3.contanos.model.request.LoginGoogleRequest;
import com.lucas3.contanos.model.request.RegisterRequest;
import com.lucas3.contanos.model.response.StandResponse;
import com.lucas3.contanos.model.request.LoginRequest;
import com.lucas3.contanos.service.UserService;
import io.swagger.annotations.Api;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Base64;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@Api(value = "Api de autenticacion", description = "Registro y login google o backoffice")
@RequestMapping("")
public class AuthController {

    @Autowired
    private UserService userService;

    @Value("${contame.app.secretAdmin}")
    private String secretAdmin;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest, @RequestHeader("Authorization") String token){
        try{
            validateToken(token);
            validateRegister(registerRequest);
            return userService.registerUser(registerRequest);
        } catch (InvalidRegisterException e) {
            return ResponseEntity.badRequest().body(new StandResponse("FALTA EMAIL O PASSWORD"));
        } catch (InvalidTokenException e) {
            return ResponseEntity.badRequest().body(new StandResponse("CREDENCIALES INVALIDAS"));
        } catch (InvalidPasswordException e) {
            return ResponseEntity.badRequest().body(new StandResponse("Password insegura"));
        } catch (EmailTakenException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El email ya se encuentra registrado en el sistema"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?>login(@RequestBody LoginRequest loginRequest){
        try{
            validateLogin(loginRequest);
            return userService.authenticateUser(loginRequest);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(new StandResponse("Las credenciales son invalidas"));
        }
    }

    @PostMapping("/login/google")
    public ResponseEntity<?>login(@RequestBody LoginGoogleRequest request){
        try{
            validateLoginGoogle(request);
            return userService.authenticateUserWithGoogle(request);
        } catch (GeneralSecurityException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El token de google es invalido"));
        } catch (InvalidLoginException e) {
            return ResponseEntity.badRequest().body(new StandResponse("Las credenciales son invalidas"));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(new StandResponse("Hubo problemas en el servicio de autenticacion"));
        }
    }

    private void validateRegister(RegisterRequest request) throws InvalidRegisterException {
        if(request.getEmail() == null || StringUtils.isEmpty(request.getEmail())) throw new InvalidRegisterException();
        if(request.getPassword() == null || StringUtils.isEmpty(request.getPassword())) throw new InvalidRegisterException();
    }

    private void validateLogin(LoginRequest request) throws InvalidLoginException {
        if(request.getEmail() == null || StringUtils.isEmpty(request.getEmail())) throw new InvalidLoginException();
        if(request.getPassword() == null || StringUtils.isEmpty(request.getPassword())) throw new InvalidLoginException();
    }

    private void validateLoginGoogle(LoginGoogleRequest request) throws InvalidLoginException {
        if(request.getToken() == null || StringUtils.isEmpty(request.getToken())) throw new InvalidLoginException();
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
