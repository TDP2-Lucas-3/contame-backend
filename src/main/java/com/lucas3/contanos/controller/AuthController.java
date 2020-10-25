package com.lucas3.contanos.controller;

import com.lucas3.contanos.model.exception.FailedToLoadImageException;
import com.lucas3.contanos.model.exception.InvalidLoginException;
import com.lucas3.contanos.model.exception.InvalidRegisterException;
import com.lucas3.contanos.model.request.LoginGoogleRequest;
import com.lucas3.contanos.model.request.RegisterRequest;
import com.lucas3.contanos.model.response.StandResponse;
import com.lucas3.contanos.model.request.LoginRequest;
import com.lucas3.contanos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@RequestMapping("")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){
        try{
            validateRegister(registerRequest);
            return userService.registerUser(registerRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new StandResponse("Tuvimos un problema registrando tu usuario, vuelve a intentarlo mas tarde"));
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



}
