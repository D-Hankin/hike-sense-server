package com.hikesenseserver.hikesenseserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hikesenseserver.hikesenseserver.models.SosMessage;
import com.hikesenseserver.hikesenseserver.services.SosService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/sos")
public class SosController {

    @Autowired
    SosService sosService;
    

    @PostMapping("/send-sos")
    public ResponseEntity<String> sendSos(@Valid @RequestBody SosMessage sosMessage) {
        System.out.println("SOS sent");
        return sosService.sendSos(sosMessage);
    }
}
