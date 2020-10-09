package com.lucas3.contanos.service;

import com.lucas3.contanos.model.request.LoginRequest;
import com.lucas3.contanos.model.request.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface IUserService {

    public ResponseEntity<?> registerUser(RegisterRequest registerRequest);

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest);


}
