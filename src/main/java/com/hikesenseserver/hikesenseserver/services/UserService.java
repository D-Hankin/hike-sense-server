package com.hikesenseserver.hikesenseserver.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hikesenseserver.hikesenseserver.components.JwtComponent;
import com.hikesenseserver.hikesenseserver.models.Hike;
import com.hikesenseserver.hikesenseserver.models.LoginDto;
import com.hikesenseserver.hikesenseserver.models.LoginResponse;
import com.hikesenseserver.hikesenseserver.models.User;
import com.hikesenseserver.hikesenseserver.repositories.UserRepository;

import jakarta.validation.Valid;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtComponent jwtCreationComponent;

    @Autowired
    private final AuthenticationManager authenticationManager;

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
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setSubscriptionStatus("none");
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
            System.out.println("User: " + user.getFirstName());
            user.setPassword(null);
            return ResponseEntity.ok(new LoginResponse(token, user));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(new LoginResponse("Bad credentials", null));
        }
    }

}
