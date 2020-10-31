package com.lucas3.contanos.service;

import com.lucas3.contanos.entities.Category;
import com.lucas3.contanos.entities.ERole;
import com.lucas3.contanos.entities.Incident;
import com.lucas3.contanos.entities.User;
import com.lucas3.contanos.model.exception.FailedToLoadImageException;
import com.lucas3.contanos.model.exception.UserNotFoundException;
import com.lucas3.contanos.model.request.CategoryRequest;
import com.lucas3.contanos.model.request.IncidentRequest;
import com.lucas3.contanos.model.request.RegisterRequest;
import com.lucas3.contanos.model.request.UpdateUserRequest;
import com.lucas3.contanos.model.response.UserResponse;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

// Test del servicio de reportes
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Test
    public void registerBackofficeTest() throws FailedToLoadImageException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "prueba123");
        userService.registerUser(register);

        UserResponse user = userService.getUserById(1L);

        Assert.assertEquals(user.getEmail(), "prueba@prueba.com");
        Assert.assertEquals(user.getRol(), ERole.ROLE_ADMIN.toString());
    }

    @Test
    public void noBackofficeUsersTest() throws FailedToLoadImageException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "prueba123");
        userService.registerUser(register);
        List<UserResponse> users = userService.getAllUsers();
        Assert.assertTrue(users.isEmpty());
    }

    @Test
    public void getUserByEmail() throws FailedToLoadImageException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "prueba123");
        userService.registerUser(register);
        UserResponse response = userService.getUserByEmail("prueba@prueba.com");
        Assert.assertEquals(response.getEmail(), "prueba@prueba.com");
        Assert.assertEquals(response.getRol(), ERole.ROLE_ADMIN.toString());
    }

    @Test
    public void updateUserTest() throws FailedToLoadImageException, UserNotFoundException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "prueba123");
        register.setName("Usuario");
        register.setSurname("Prueba1");
        userService.registerUser(register);

        UpdateUserRequest update = new UpdateUserRequest();
        update.setName("update");
        update.setSurname("prueba2");
        userService.updateUserProfile(update,"prueba@prueba.com");

        UserResponse user = userService.getUserById(1L);

        Assert.assertEquals(user.getEmail(), "prueba@prueba.com");
        Assert.assertEquals(user.getRol(), ERole.ROLE_ADMIN.toString());
        Assert.assertEquals(user.getName(), "update");
        Assert.assertEquals(user.getSurname(), "prueba2");
    }


    @Test
    public void updateUserWithoutProfileTest() throws FailedToLoadImageException, UserNotFoundException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "prueba123");
        userService.registerUser(register);

        UpdateUserRequest update = new UpdateUserRequest();
        update.setName("update");
        update.setSurname("prueba2");
        userService.updateUserProfile(update,"prueba@prueba.com");

        UserResponse user = userService.getUserById(1L);
        
        Assert.assertEquals(user.getEmail(), "prueba@prueba.com");
        Assert.assertEquals(user.getRol(), ERole.ROLE_ADMIN.toString());
        Assert.assertEquals(user.getName(), "update");
        Assert.assertEquals(user.getSurname(), "prueba2");
    }
}
