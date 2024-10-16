package com.hikesenseserver.hikesenseserver.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.hikesenseserver.hikesenseserver.components.JwtComponent;
import com.hikesenseserver.hikesenseserver.models.Hike;
import com.hikesenseserver.hikesenseserver.models.LoginDto;
import com.hikesenseserver.hikesenseserver.models.LoginResponse;
import com.hikesenseserver.hikesenseserver.models.User;
import com.hikesenseserver.hikesenseserver.repositories.UserRepository;

import jakarta.validation.Valid;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class UserController {

     @Autowired
    UserRepository userRepository;

    @Autowired
    JwtComponent jwtCreationComponent;

    @Autowired
    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    public UserController(PasswordEncoder passwordEncoder) {
        this.authenticationManager = null;
        this.passwordEncoder = passwordEncoder;
    }
    
    @PostMapping("/create-account")
    public ResponseEntity<User> createAccount(@Valid @RequestBody User user) {
        if (!user.getUsername().isEmpty() || !user.getFirstName().isEmpty() || !user.getLastName().isEmpty() || !user.getPassword().isEmpty()) {

            user.setHikes(new ArrayList<Hike>());
            user.setFriends(new ArrayList<>());
            
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User createdUser = userRepository.insert(user);
            return ResponseEntity.ok(createdUser);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    user.getUsername(), 
                    user.getPassword())
            );
            String token = jwtCreationComponent.createToken(authentication);
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (AuthenticationException e) {
        
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(new LoginResponse("Bad credentials"));
        }
    }
    
}
