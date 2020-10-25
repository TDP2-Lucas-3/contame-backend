package com.lucas3.contanos.service;

import com.lucas3.contanos.entities.User;
import com.lucas3.contanos.model.exception.FailedToLoadImageException;
import com.lucas3.contanos.model.exception.InvalidLoginException;
import com.lucas3.contanos.model.exception.UserNotFoundException;
import com.lucas3.contanos.model.request.LoginGoogleRequest;
import com.lucas3.contanos.model.request.LoginRequest;
import com.lucas3.contanos.model.request.RegisterRequest;
import com.lucas3.contanos.model.request.UpdateUserRequest;
import com.lucas3.contanos.model.response.UserResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;


public interface IUserService {

    ResponseEntity<?> registerUser(RegisterRequest registerRequest) throws FailedToLoadImageException;

    ResponseEntity<?> authenticateUser(LoginRequest loginRequest);

    ResponseEntity<?> authenticateUserWithGoogle(LoginGoogleRequest loginRequest) throws GeneralSecurityException, IOException, InvalidLoginException, FailedToLoadImageException;

    List<UserResponse> getAllUsers();

    User getUserById(Long id);

    User updateUserProfile(UpdateUserRequest request, String email) throws FailedToLoadImageException, UserNotFoundException;

}
