package com.hikesenseserver.hikesenseserver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.hikesenseserver.hikesenseserver.components.JwtComponent;
import com.hikesenseserver.hikesenseserver.models.LoginDto;
import com.hikesenseserver.hikesenseserver.models.LoginResponse;
import com.hikesenseserver.hikesenseserver.models.User;
import com.hikesenseserver.hikesenseserver.models.UserDto;
import com.hikesenseserver.hikesenseserver.repositories.UserRepository;
import com.hikesenseserver.hikesenseserver.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtComponent jwtComponent;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Test
    public void testCreateAccountAndLogin() {
        // Arrange
        User newUser = new User();
        newUser.setUsername("test@example.com");
        newUser.setPassword("plainPassword"); 
    
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(newUser.getPassword())).thenReturn(encodedPassword);
    
        newUser.setPassword(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        when(userRepository.findByUsername(newUser.getUsername())).thenReturn(Optional.of(newUser));
    
        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuth);
        when(jwtComponent.createToken(mockAuth)).thenReturn("testToken");
    
        // Act
        ResponseEntity<LoginResponse> response = userService.createAccount(newUser);
    
        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("testToken", response.getBody().getToken());
        assertEquals(newUser, response.getBody().getUser());
    }
    

    @Test
    public void testLoginSuccessful() {
        // Arrange
        LoginDto loginDto = new LoginDto("test@example.com", "password123");
        User user = new User();
        user.setUsername("test@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername(loginDto.getUsername())).thenReturn(Optional.of(user));

        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuth);
        when(jwtComponent.createToken(mockAuth)).thenReturn("testToken");

        // Act
        ResponseEntity<LoginResponse> response = userService.login(loginDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("testToken", response.getBody().getToken());
        assertEquals(user, response.getBody().getUser());
    }

    @Test
    public void testLoginInvalidCredentials() {
        // Arrange
        LoginDto loginDto = new LoginDto("test@example.com", "wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // Act
        ResponseEntity<LoginResponse> response = userService.login(loginDto);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Bad credentials", response.getBody().getToken());
    }


   @Test
    public void testGetUserSuccessful() {
        // Arrange
        User user = new User();
        user.setUsername("test@example.com");
        user.setPassword("encodedPassword");
        
        UserDto userDto = new UserDto(user); 

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getPrincipal()).thenReturn(userDto); 
        SecurityContextHolder.getContext().setAuthentication(mockAuth);

        // Act
        ResponseEntity<User> response = userService.getUser();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(user, response.getBody());
    }


}
