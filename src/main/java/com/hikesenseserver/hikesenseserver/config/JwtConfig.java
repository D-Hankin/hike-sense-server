package com.hikesenseserver.hikesenseserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    
    @Value("${PRIVATE_KEY}")
    private String privateKey;
    
    @Value("${JWT_ISSUER}")
    private String jwtIssuer;

    @Value("{ARDUINO_IP}")
    private String arduinoIp;

    public String getPrivateKey() {
        return privateKey;
    }

    public String getJwtIssuer() {
        return jwtIssuer;
    }

    public String getArduinoIp() {
        return arduinoIp;
    }
}
