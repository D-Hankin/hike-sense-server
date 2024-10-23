package com.hikesenseserver.hikesenseserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {
    
    @Value("${STRIPE_SECRET_KEY}")
    private String stripeSecretKey;

    public String getStripeSecretKey() {
        return stripeSecretKey;
    }

}
