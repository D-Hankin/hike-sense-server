package com.hikesenseserver.hikesenseserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {
    
    @Value("${EMAIL_USERNAME}")
    private String emailUsername;

    @Value("${EMAIL_PASSWORD}")
    private String emailPassword;

    @Value("${EMAIL_HOST}")
    private String emailHost;

    @Value("${EMAIL_PORT}")
    private String emailPort;

    public String getEmailUsername() {
        return emailUsername;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public String getEmailHost() {
        return emailHost;
    }

    public String getEmailPort() {
        return emailPort;
    }

        
}
