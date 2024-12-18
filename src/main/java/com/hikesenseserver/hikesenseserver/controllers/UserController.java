package com.hikesenseserver.hikesenseserver.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.hikesenseserver.hikesenseserver.components.JwtComponent;
import com.hikesenseserver.hikesenseserver.models.LoginDto;
import com.hikesenseserver.hikesenseserver.models.LoginResponse;
import com.hikesenseserver.hikesenseserver.models.User;
import com.hikesenseserver.hikesenseserver.models.UserOnline;
import com.hikesenseserver.hikesenseserver.services.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;



@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    JwtComponent jwtCreationComponent;

    @Autowired
    UserService userService;
    
    @PostMapping("/create-account")
    public ResponseEntity<LoginResponse> createAccount(@Valid @RequestBody User user) {
        return userService.createAccount(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginDto user) {
        return userService.login(user);
    }

    @GetMapping("/get-user")
    public ResponseEntity<User> getUser() {
        return userService.getUser();
    }  

    @GetMapping("/users-online")
    public List<UserOnline> getMethodName() {
        return userService.getUsersOnline();
    }
    
    
}
