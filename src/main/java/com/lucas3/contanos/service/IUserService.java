package com.lucas3.contanos.service;

import com.lucas3.contanos.entities.User;
import com.lucas3.contanos.model.exception.FailedToLoadImageException;
import com.lucas3.contanos.model.request.LoginRequest;
import com.lucas3.contanos.model.request.RegisterRequest;
import org.springframework.http.ResponseEntity;
import java.util.List;


public interface IUserService {

    ResponseEntity<?> registerUser(RegisterRequest registerRequest) throws FailedToLoadImageException;

    ResponseEntity<?> authenticateUser(LoginRequest loginRequest);

    List<User> getAllUsers();

    User getUserById(Long id);

}
