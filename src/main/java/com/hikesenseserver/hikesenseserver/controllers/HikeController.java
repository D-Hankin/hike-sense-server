package com.hikesenseserver.hikesenseserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hikesenseserver.hikesenseserver.models.Hike;
import com.hikesenseserver.hikesenseserver.models.User;
import com.hikesenseserver.hikesenseserver.services.HikeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/hike")
public class HikeController {

    @Autowired
    HikeService hikeService;

    @PostMapping("/new-hike")
    public ResponseEntity<User> newHike(@Valid @RequestBody Hike hike) {
        return hikeService.newHike(hike);
    }
    
}