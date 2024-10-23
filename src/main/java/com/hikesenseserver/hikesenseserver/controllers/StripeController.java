package com.hikesenseserver.hikesenseserver.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hikesenseserver.hikesenseserver.config.StripeConfig;
import com.hikesenseserver.hikesenseserver.models.CustomerAddress;
import com.hikesenseserver.hikesenseserver.services.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("/stripe")
public class StripeController {

    @Autowired
    StripeService stripeService;

    @Autowired
    StripeConfig stripeConfig;

    @PostMapping("/upgrade")
    public ResponseEntity<Map<String, String>> upgradeSubscription(@RequestHeader String productId, @RequestBody CustomerAddress address) throws StripeException {
        Stripe.apiKey = stripeConfig.getStripeSecretKey();
        Map<String, String> response = stripeService.upgradeSubscription(productId, address);
        return ResponseEntity.ok(response);
    }
    
}
