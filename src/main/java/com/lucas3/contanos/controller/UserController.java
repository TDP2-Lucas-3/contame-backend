package com.lucas3.contanos.controller;

import com.lucas3.contanos.entities.User;
import com.lucas3.contanos.model.request.UpdateUserRequest;
import com.lucas3.contanos.model.response.StandResponse;
import com.lucas3.contanos.model.response.UserResponse;
import com.lucas3.contanos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET})
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public List<UserResponse> getUsers(){
        List<UserResponse> response = new ArrayList<>();
        List<User> users = userService.getAllUsers();
        for (User user: users) {
            response.add(new UserResponse(user));
        }
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id)  {
        try{
            return ResponseEntity.ok(new UserResponse(userService.getUserById(id)));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new StandResponse("El usuario no existe"));
        }
    }

    @PutMapping("")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest update){
        try{
            return ResponseEntity.ok(new UserResponse(userService.updateUserProfile(update)));
        }catch(Exception e){
            return ResponseEntity.badRequest().body(new StandResponse("No se pudo actualizar el perfil del usuario"));
        }

    }
}
