package com.hikesenseserver.hikesenseserver.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hikesenseserver.hikesenseserver.components.JwtComponent;
import com.hikesenseserver.hikesenseserver.models.FriendRequest;
import com.hikesenseserver.hikesenseserver.models.Hike;
import com.hikesenseserver.hikesenseserver.models.LoginDto;
import com.hikesenseserver.hikesenseserver.models.LoginResponse;
import com.hikesenseserver.hikesenseserver.models.User;
import com.hikesenseserver.hikesenseserver.models.UserOnline;
import com.hikesenseserver.hikesenseserver.repositories.UserRepository;
import com.hikesenseserver.hikesenseserver.repositories.UserOnlineRepository;

import jakarta.validation.Valid;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtComponent jwtCreationComponent;

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    UserOnlineRepository userOnlineRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder) {
        this.authenticationManager = null;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<LoginResponse> createAccount(@Valid User user) {

        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body(new LoginResponse("email already in use.", null));
        }
        
        if (!user.getUsername().isEmpty() || !user.getFirstName().isEmpty() || !user.getLastName().isEmpty() || !user.getPassword().isEmpty()) {

            String rawPassword = user.getPassword();

            user.setHikes(new ArrayList<Hike>());
            user.setFriends(new ArrayList<>());
            user.setPendingFriendRequests(new ArrayList<>());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setSubscriptionStatus("free");
            user.setAuthorities(List.of("ROLE_USER"));
            userRepository.insert(user);

            ResponseEntity<LoginResponse> response = login(new LoginDto(user.getUsername(), rawPassword));
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .body(response.getBody());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new LoginResponse("Invalid user data", null));
    }

    public ResponseEntity<LoginResponse> login(@Valid LoginDto userDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    userDto.getUsername(), 
                    userDto.getPassword())
            );
            String token = jwtCreationComponent.createToken(authentication);
            User user = userRepository.findByUsername(userDto.getUsername()).get();
            user.setPassword(null);
            return ResponseEntity.ok(new LoginResponse(token, user));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(new LoginResponse("Bad credentials", null));
        }
    }

    public ResponseEntity<User> getUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    public void addFriendRequest(FriendRequest request) {
        User receiver = userRepository.findByUsername(request.getReceiver())
                                      .orElseThrow(() -> new IllegalArgumentException("User not found"));
        receiver.getPendingFriendRequests().add(request.getSender());
        userRepository.save(receiver);
    }

    public void addUserOnline(String username) {
        // Check if the user is already online
        List<UserOnline> usersOnline = userOnlineRepository.findAll();
        boolean userExists = usersOnline.stream().anyMatch(user -> user.getUsername().equals(username));
        
        if (!userExists) {
            // Create a new UserOnline instance and save it
            UserOnline newUserOnline = new UserOnline(username);
            userOnlineRepository.save(newUserOnline);
            System.out.println(username + " is now online.");
        } else {
            System.out.println(username + " is already online.");
        }
    }

    // Removes a user from the online users collection
    public void removeUserOnline(String username) {   
        // Find the user in the repository
        UserOnline userToRemove = userOnlineRepository.findByUsername(username);
        
        if (userToRemove != null) {
            userOnlineRepository.delete(userToRemove);  // Delete user from repository
            System.out.println(username + " is now offline.");
        } else {
            System.out.println(username + " was not found in the online list.");
        }
    }
    

    // Retrieves all users currently online
    public List<UserOnline> getUsersOnline() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();
    
        System.out.println("Authenticated User: " + userDetails.getUsername());
    
        User user = userRepository.findByUsername(userDetails.getUsername())
                                 .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    
        List<UserOnline> usersOnline = userOnlineRepository.findAll();
    
        System.out.println("Users online before filtering: " + usersOnline.stream()
                                                                           .map(UserOnline::getUsername)
                                                                           .collect(Collectors.joining(", ")));
    
        // Iterate with explicit conditions
        Iterator<UserOnline> iterator = usersOnline.iterator();
        while (iterator.hasNext()) {
            UserOnline userOnline = iterator.next();
            boolean isLoggedInUser = user.getUsername().equals(userOnline.getUsername());
            System.out.println("Checking UserOnline: " + userOnline.getUsername());

            if (isLoggedInUser) {
                iterator.remove();
                System.out.println("Removed: " + userOnline.getUsername());
                continue;
            }

            boolean isFriend = user.getFriends().stream()
                                   .anyMatch(friend -> friend.getUsernameFriend().equals(userOnline.getUsername().toString()));
    
            System.out.println("Is Logged-in User: " + isLoggedInUser + ", Is Friend: " + isFriend);
    
            // Remove if not the current user and not a friend
            if (!isFriend) {
                iterator.remove();
                System.out.println("Removed: " + userOnline.getUsername());
            }
        }
    
        System.out.println("Users online after filtering: " + usersOnline.stream()
                                                                          .map(UserOnline::getUsername)
                                                                          .collect(Collectors.joining(", ")));
        return usersOnline;
    }
    
    
}