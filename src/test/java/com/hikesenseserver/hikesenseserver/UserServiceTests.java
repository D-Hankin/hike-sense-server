package com.hikesenseserver.hikesenseserver;

import com.hikesenseserver.hikesenseserver.components.JwtComponent;
import com.hikesenseserver.hikesenseserver.models.FriendRequest;
import com.hikesenseserver.hikesenseserver.models.LoginDto;
import com.hikesenseserver.hikesenseserver.models.LoginResponse;
import com.hikesenseserver.hikesenseserver.models.User;
import com.hikesenseserver.hikesenseserver.models.UserOnline;
import com.hikesenseserver.hikesenseserver.repositories.UserOnlineRepository;
import com.hikesenseserver.hikesenseserver.repositories.UserRepository;
import com.hikesenseserver.hikesenseserver.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;  // Mocking the User repository

    @Mock
    private JwtComponent jwtCreationComponent;  // Mocking JWT creation component

    @Mock
    private AuthenticationManager authenticationManager;  // Mocking Authentication Manager

    @Mock
    private UserOnlineRepository userOnlineRepository;  // Mocking User Online repository

    @Mock
    private PasswordEncoder passwordEncoder;  // Mocking Password Encoder

    @InjectMocks
    private UserService userService;  // Injecting mocks into UserService for testing

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Initializing mocks before each test
    }

    @Test
    public void testCreateAccount_UserExists() {
        User user = new User();
        user.setUsername("test@example.com");  // Creating a test user

        when(userRepository.existsByUsername("test@example.com")).thenReturn(true);  // Mocking user existence check

        ResponseEntity<LoginResponse> response = userService.createAccount(user);  // Calling the method to test

        assertEquals(409, response.getStatusCode());  // Asserting conflict status
        // Commented out as getMessage() doesn't exist
        // assertEquals("email already in use.", response.getBody().getMessage());
    }

    @Test
    public void testCreateAccount_Success() {
        User user = new User();
        user.setUsername("test@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword("password");  // Setting user details

        when(userRepository.existsByUsername("test@example.com")).thenReturn(false);  // User doesn't exist
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");  // Mocking password encoding

        // Mocking the successful login response
        LoginResponse loginResponse = new LoginResponse("token", user);
        when(userRepository.insert(any(User.class))).thenReturn(user);  // Mocking user insertion
        when(userService.login(any(LoginDto.class))).thenReturn(ResponseEntity.ok(loginResponse));  // Mocking login response

        ResponseEntity<LoginResponse> response = userService.createAccount(user);  // Calling the method to test

        assertEquals(201, response.getStatusCode());  // Asserting success status
        assertNotNull(response.getBody());  // Asserting response body is not null
        assertEquals("token", response.getBody().getToken());  // Asserting token is as expected
    }

    @Test
    public void testLogin_Success() {
        LoginDto loginDto = new LoginDto("test@example.com", "password");  // Creating login DTO
        User user = new User();
        user.setUsername("test@example.com");  // Creating a test user

        Authentication authentication = mock(Authentication.class);  // Mocking authentication
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);  // Mocking authentication manager
        when(jwtCreationComponent.createToken(authentication)).thenReturn("token");  // Mocking token creation
        when(userRepository.findByUsername("test@example.com")).thenReturn(Optional.of(user));  // Mocking user retrieval

        ResponseEntity<LoginResponse> response = userService.login(loginDto);  // Calling the method to test

        assertEquals(200, response.getStatusCode());  // Asserting success status
        assertNotNull(response.getBody());  // Asserting response body is not null
        assertEquals("token", response.getBody().getToken());  // Asserting token is as expected
        assertEquals(user, response.getBody().getUser());  // Asserting returned user matches
    }

    @Test
    public void testLogin_BadCredentials() {
        LoginDto loginDto = new LoginDto("test@example.com", "wrongPassword");  // Creating login DTO with wrong password

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));  // Mocking bad credentials exception

        ResponseEntity<LoginResponse> response = userService.login(loginDto);  // Calling the method to test

        assertEquals(401, response.getStatusCode());  // Asserting unauthorized status
        // No message assertion since getMessage() is not part of LoginResponse
    }

    @Test
public void testGetUser_Success() {
    // Create a mock Authentication object
    Authentication authentication = mock(Authentication.class);
    
    // Mock the username retrieval from the Authentication object
    when(authentication.getName()).thenReturn("test@example.com");
    
    // Set the mocked Authentication object in the SecurityContext
    SecurityContextHolder.getContext().setAuthentication(authentication);
    
    // Create a test user
    User user = new User();
    user.setUsername("test@example.com");  // Set username
    
    // Mock user retrieval from the userRepository
    when(userRepository.findByUsername("test@example.com")).thenReturn(Optional.of(user));
    
    // Call the method to test
    ResponseEntity<User> response = userService.getUser();
    
    // Assertions to verify the response
    assertNotNull(response);  // Asserting response is not null
    assertEquals("test@example.com", response.getBody().getUsername());  // Asserting returned username matches
}


    @Test
    public void testAddFriendRequest_Success() {
        FriendRequest request = new FriendRequest("sender", "receiver", "status");  // Creating a test friend request

        User receiver = new User();
        receiver.setUsername("receiver");
        receiver.setPendingFriendRequests(new ArrayList<>());  // Initializing pending friend requests

        when(userRepository.findByUsername("receiver")).thenReturn(Optional.of(receiver));  // Mocking user retrieval

        userService.addFriendRequest(request);  // Calling the method to test

        assertEquals(1, receiver.getPendingFriendRequests().size());  // Asserting the size of pending requests
        assertTrue(receiver.getPendingFriendRequests().contains("sender"));  // Asserting sender is in pending requests
        verify(userRepository).save(receiver);  // Verifying user save method was called
    }

    @Test
    public void testAddUserOnline_Success() {
        String username = "testUser";  // Creating a test username
        when(userOnlineRepository.findAll()).thenReturn(new ArrayList<>());  // Mocking empty online user list

        userService.addUserOnline(username);  // Calling the method to test

        verify(userOnlineRepository).save(any(UserOnline.class));  // Verifying save method was called
    }

    @Test
    public void testRemoveUserOnline_UserExists() {
        String username = "testUser";  // Creating a test username
        UserOnline userOnline = new UserOnline(username);  // Creating a test UserOnline instance

        when(userOnlineRepository.findByUsername(username)).thenReturn(userOnline);  // Mocking user retrieval

        userService.removeUserOnline(username);  // Calling the method to test

        verify(userOnlineRepository).delete(userOnline);  // Verifying delete method was called
    }

    @Test
    public void testRemoveUserOnline_UserDoesNotExist() {
        String username = "nonExistentUser";  // Creating a non-existent username
        when(userOnlineRepository.findByUsername(username)).thenReturn(null);  // Mocking non-existent user retrieval

        userService.removeUserOnline(username);  // Calling the method to test

        verify(userOnlineRepository, never()).delete(any());  // Verifying delete method was never called
    }

    @Test
    public void testGetUsersOnline_Success() {
        String loggedInUser = "testUser";  // Creating a logged-in username
        UserDetails userDetails = mock(UserDetails.class);  // Mocking user details
        when(userDetails.getUsername()).thenReturn(loggedInUser);  // Mocking username retrieval
        SecurityContextHolder.getContext().setAuthentication(mock(Authentication.class));  // Setting authentication

        User user = new User();
        user.setUsername(loggedInUser);  // Creating a test user
        when(userRepository.findByUsername(loggedInUser)).thenReturn(Optional.of(user));  // Mocking user retrieval

        List<UserOnline> onlineUsers = new ArrayList<>();  // Creating an online users list
        when(userOnlineRepository.findAll()).thenReturn(onlineUsers);  // Mocking online user retrieval

        List<UserOnline> response = userService.getUsersOnline();  // Calling the method to test

        assertNotNull(response);  // Asserting response body is not null
    }
}
