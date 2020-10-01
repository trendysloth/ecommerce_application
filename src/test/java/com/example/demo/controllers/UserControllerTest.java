package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.shortThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() throws IllegalAccessException {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUser() throws Exception {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    public void createUserFailure() throws Exception {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test2");
        r.setPassword("test2");
        r.setConfirmPassword("test");
        final ResponseEntity<User> response = userController.createUser(r);
        Assert.assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void getUserByUsername() {
        User user = new User();
        user.setId(0);
        user.setUsername("test");
        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);

        final ResponseEntity<User> response = userController.findByUserName("test");

        assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
    }

    @Test
    public void getUserById() {
        User user = new User();
        user.setId(0);
        user.setUsername("test");
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));

        final ResponseEntity<User> response = userController.findById(0L);

        assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
    }
}