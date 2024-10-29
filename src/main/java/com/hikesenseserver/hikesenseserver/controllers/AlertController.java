package com.hikesenseserver.hikesenseserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hikesenseserver.hikesenseserver.models.HeartRateAlert;
import com.hikesenseserver.hikesenseserver.services.AlertService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/alert")
public class AlertController {
    
    @Autowired
    AlertService alertService;

    @PostMapping("/send-alert")
    public ResponseEntity<String> sendAlert(@Valid @RequestBody HeartRateAlert alert) {
        System.out.println("Alert sent");
        return alertService.sendAlert(alert);
    }
}
