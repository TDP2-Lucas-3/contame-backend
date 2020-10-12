package com.lucas3.contanos.controller;

import com.lucas3.contanos.model.request.RegisterRequest;
import com.lucas3.contanos.model.response.StandResponse;
import com.lucas3.contanos.model.request.LoginRequest;
import com.lucas3.contanos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest loginRequest){
        return userService.registerUser(loginRequest);

    }
    @PostMapping("/login")
    public ResponseEntity<?>login(@RequestBody LoginRequest loginRequest){
        return userService.authenticateUser(loginRequest);

    }



}
