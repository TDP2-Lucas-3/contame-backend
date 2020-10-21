package com.lucas3.contanos.service;

import com.lucas3.contanos.entities.Category;
import com.lucas3.contanos.entities.ERole;
import com.lucas3.contanos.entities.Incident;
import com.lucas3.contanos.entities.User;
import com.lucas3.contanos.model.exception.FailedToLoadImageException;
import com.lucas3.contanos.model.request.CategoryRequest;
import com.lucas3.contanos.model.request.IncidentRequest;
import com.lucas3.contanos.model.request.RegisterRequest;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.List;

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

        User user = userService.getUserById(1L);

        Assert.assertEquals(user.getEmail(), "prueba@prueba.com");
        Assert.assertEquals(user.getRol(), ERole.ROLE_ADMIN);
    }

    @Test
    public void noBackofficeUsersTest() throws FailedToLoadImageException {
        RegisterRequest register = new RegisterRequest("prueba@prueba.com", "prueba123");
        userService.registerUser(register);
        List<User> users = userService.getAllUsers();
        Assert.assertTrue(users.isEmpty());
    }
}
