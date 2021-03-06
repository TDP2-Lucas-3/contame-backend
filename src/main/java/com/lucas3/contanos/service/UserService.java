package com.lucas3.contanos.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.lucas3.contanos.entities.ERole;
import com.lucas3.contanos.entities.EUserState;
import com.lucas3.contanos.entities.Profile;
import com.lucas3.contanos.entities.User;
import com.lucas3.contanos.model.exception.*;
import com.lucas3.contanos.model.request.LoginGoogleRequest;
import com.lucas3.contanos.model.request.UpdateUserRequest;
import com.lucas3.contanos.model.response.LoginGoogleResponse;
import com.lucas3.contanos.model.response.LoginResponse;
import com.lucas3.contanos.model.request.RegisterRequest;
import com.lucas3.contanos.model.response.StandResponse;
import com.lucas3.contanos.model.request.LoginRequest;
import com.lucas3.contanos.model.response.UserResponse;
import com.lucas3.contanos.repository.IncidentRepository;
import com.lucas3.contanos.repository.ProfileRepository;
import com.lucas3.contanos.repository.UserRepository;
import com.lucas3.contanos.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class UserService implements IUserService{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ImgbbService imgbbService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${contame.app.google.client.id}")
    private String CLIENT_ID_GOOGLE;

    private static  String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";


    @Override
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());

        return ResponseEntity.ok(new LoginResponse(jwt,
                user.get().getId(),
                user.get().getEmail(),
                user.get().getRol().toString()));
    }

    @Override
    public ResponseEntity<?> authenticateUserWithGoogle(LoginGoogleRequest request) throws GeneralSecurityException, IOException, InvalidLoginException {
        User user = authenticateWithGoogle(request);

        String jwt = jwtUtils.generateJwtTokenGoogle(user);

        return ResponseEntity.ok(new LoginGoogleResponse(jwt,
                user.getId(),
                user.getEmail(),
                user.getRol().toString(),
                user.getLastLoginDate() == null));
    }

    private User authenticateWithGoogle(LoginGoogleRequest request) throws GeneralSecurityException, IOException, InvalidLoginException{

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(CLIENT_ID_GOOGLE))
                .build();

        GoogleIdToken idToken = verifier.verify(request.getToken());
        if (idToken != null) {
            Payload payload = idToken.getPayload();
            String email = payload.getEmail();

            Optional<User> user = userRepository.findByEmail(email);

            if(user.isPresent()){
                user.get().setLastLoginDate(new Date());
                user.get().setFCMToken(request.getFirebaseToken());
                userRepository.save(user.get());
                return user.get();
            }else{
                User newUser = new User(email);

                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");
                String familyName = (String) payload.get("family_name");

                Profile newProfile = new Profile(name, familyName,pictureUrl);
                profileRepository.save(newProfile);
                newUser.setProfile(newProfile);
                newUser.setRol(ERole.ROLE_USER);
                newUser.setUserState(EUserState.ACTIVO);
                newUser.setRegisterDate(new Date());
                newUser.setFCMToken(request.getFirebaseToken());
                userRepository.save(newUser);
                return newUser;
            }
        } else {
            throw new InvalidLoginException();
        }
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<UserResponse> response = new ArrayList<>();
        List<User> users =  userRepository.findAllByRol(ERole.ROLE_USER);
        for (User user: users) {
                UserResponse userResponse = new UserResponse(user);
                userResponse.setIncidentCount(incidentRepository.countByUser(user));
                response.add(userResponse);
        }
        return response;


    }

    @Override
    public UserResponse getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            UserResponse response =  new UserResponse(user.get());
            response.setIncidentCount(incidentRepository.countByUser(user.get()));
            return response;
        }
        throw new UsernameNotFoundException("El usuario no existe");
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            UserResponse response =  new UserResponse(user.get());
            response.setIncidentCount(incidentRepository.countByUser(user.get()));
            return response;
        }
        throw new UsernameNotFoundException("El usuario no existe");
    }

    @Override
    @Transactional
    public User updateUserProfile(UpdateUserRequest request, String email) throws FailedToLoadImageException, UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);

        if(user.isPresent()){
            if(user.get().getProfile() != null){
                user.get().getProfile().setName(request.getName());
                user.get().getProfile().setSurename(request.getSurname());
            }else{
                Profile newProfile = new Profile(request.getName(),request.getSurname());
                profileRepository.save(newProfile);
                user.get().setProfile(newProfile);
            }
            if( request.getPhoto() != null && !request.getPhoto().isEmpty()){
                user.get().getProfile().setPhoto(imgbbService.uploadImgToImgbb(request.getPhoto()));
            }
            return user.get();
        }
        throw new UserNotFoundException();
    }

    @Override
    @Transactional
    public ResponseEntity<?> registerUser(RegisterRequest signUpRequest) throws InvalidPasswordException, EmailTakenException {

        if (userRepository.existsByEmail(signUpRequest.getEmail())) throw new EmailTakenException();
        if(!isValidPassword(signUpRequest.getPassword())) throw new InvalidPasswordException();

        User user = new User(signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        user.setUserState(EUserState.ACTIVO);
        userRepository.save(user);

        Profile profile = new Profile(signUpRequest.getName(), signUpRequest.getSurname());
        profileRepository.save(profile);
        user.setProfile(profile);
        user.setRol(ERole.ROLE_ADMIN);

        return ResponseEntity.ok(new StandResponse("Registro exitoso!"));
    }

    private boolean isValidPassword(String password) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        return m.matches();
    }




}
