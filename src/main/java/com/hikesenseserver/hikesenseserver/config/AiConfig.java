package com.hikesenseserver.hikesenseserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {
    
    @Value("${AI_API_KEY}")
    private String apiKey;

    @Value("$AI_API_URL")
    private String apiUrl;

    public String getApiUrl() {
        return apiUrl;
    }

    public String getApiKey() {
        return apiKey;
    }
}
