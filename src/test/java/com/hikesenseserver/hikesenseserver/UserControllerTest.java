package com.hikesenseserver.hikesenseserver;

import com.hikesenseserver.hikesenseserver.components.JwtComponent;
import com.hikesenseserver.hikesenseserver.controllers.UserController;
import com.hikesenseserver.hikesenseserver.models.LoginDto;
import com.hikesenseserver.hikesenseserver.models.LoginResponse;
import com.hikesenseserver.hikesenseserver.models.User;
import com.hikesenseserver.hikesenseserver.models.UserOnline;
import com.hikesenseserver.hikesenseserver.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private JwtComponent jwtComponent;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createAccountShouldReturnCreatedResponse() {
        //Arrange
        User user = new User();
        user.setUsername("test@example.com");
        user.setPassword("password123");
        user.setFirstName("Test");
        user.setLastName("User");

        LoginResponse loginResponse = new LoginResponse("token", user);
        when(userService.createAccount(any(User.class))).thenReturn(ResponseEntity.status(201).body(loginResponse));

        //Act
        ResponseEntity<LoginResponse> response = userController.createAccount(user);

        //Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("token", response.getBody().getToken());
        assertEquals("test@example.com", response.getBody().getUser().getUsername());
    }

    @Test
    public void loginShouldReturnOkResponse() {
        //Arrange
        LoginDto loginDto = new LoginDto("test@example.com", "password123");
        User user = new User();
        user.setUsername("test@example.com");

        LoginResponse loginResponse = new LoginResponse("token", user);
        when(userService.login(any(LoginDto.class))).thenReturn(ResponseEntity.ok(loginResponse));

        //Act
        ResponseEntity<LoginResponse> response = userController.login(loginDto);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("token", response.getBody().getToken());
        assertEquals("test@example.com", response.getBody().getUser().getUsername());
    }

    @Test
    public void getUserShouldReturnUserResponse() {
        //Arrange
        User user = new User();
        user.setUsername("test@example.com");
        when(userService.getUser()).thenReturn(ResponseEntity.ok(user));

        //Act
        ResponseEntity<User> response = userController.getUser();

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test@example.com", response.getBody().getUsername());
    }

    @Test
    public void getUsersOnlineShouldReturnUsersOnline() {
        //Arrange
        List<UserOnline> usersOnline = List.of(new UserOnline("test@example.com"));
        when(userService.getUsersOnline()).thenReturn(usersOnline);

        //Act
        List<UserOnline> response = userController.getMethodName();

        //Assert
        assertEquals(1, response.size());
        assertEquals("test@example.com", response.get(0).getUsername());
    }
}

