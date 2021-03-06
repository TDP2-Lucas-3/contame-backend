package com.lucas3.contanos.controller;

import com.lucas3.contanos.model.exception.FailedToLoadImageException;
import com.lucas3.contanos.model.exception.InvalidUpdateException;
import com.lucas3.contanos.model.exception.UserNotFoundException;
import com.lucas3.contanos.model.request.UpdateUserRequest;
import com.lucas3.contanos.model.response.StandResponse;
import com.lucas3.contanos.model.response.UserResponse;
import com.lucas3.contanos.security.jwt.JwtUtils;
import com.lucas3.contanos.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET})
@Api(value = "Api de usuarios", description = "Obtencion y modificacion de usuarios")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("")
    public List<UserResponse> getUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id)  {
        try{
            return ResponseEntity.ok(userService.getUserById(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new StandResponse("El usuario no existe"));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserById(@RequestHeader("Authorization") String fullToken)  {
        try{
            String email = jwtUtils.getUserEmailFromJwtToken(fullToken);
            return ResponseEntity.ok(userService.getUserByEmail(email));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new StandResponse("El usuario no existe"));
        }
    }

    @PutMapping("")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String fullToken, @RequestBody UpdateUserRequest update){
        try{
            validateUpdate(update);
            String email = jwtUtils.getUserEmailFromJwtToken(fullToken);
            return ResponseEntity.ok(new UserResponse(userService.updateUserProfile(update, email)));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(new StandResponse("El usuario no existe"));
        } catch (FailedToLoadImageException e) {
            return ResponseEntity.badRequest().body(new StandResponse("Hubo un error actualizando tu foto de perfil"));
        } catch (InvalidUpdateException e) {
            return ResponseEntity.badRequest().body(new StandResponse("Los campos name y surname no pueden ser nulos"));
        }
    }

    private void validateUpdate(UpdateUserRequest request) throws InvalidUpdateException {
        if(request.getName() == null || StringUtils.isEmpty(request.getName())) throw new InvalidUpdateException();
        if(request.getSurname() == null || StringUtils.isEmpty(request.getSurname())) throw new InvalidUpdateException();
    }
}
