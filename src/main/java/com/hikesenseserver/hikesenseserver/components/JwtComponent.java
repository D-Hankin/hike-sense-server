package com.hikesenseserver.hikesenseserver.components;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.hikesenseserver.hikesenseserver.config.JwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtComponent {
    
    private final JwtConfig jwtConfig; 
    private final String issuer;

    public JwtComponent(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.issuer = jwtConfig.getJwtIssuer();

        String privateKey = jwtConfig.getPrivateKey();

        if (privateKey == null || privateKey.isEmpty()) {
            throw new IllegalArgumentException("Private key is null or empty!");
        }
    }

    private SecretKey getSigningKey() {
        String keyString = jwtConfig.getPrivateKey();
        byte[] keyBytes = keyString.getBytes(StandardCharsets.UTF_8); 
        return Keys.hmacShaKeyFor(keyBytes); 
    }

    public String createToken(Authentication authentication) {

        return Jwts.builder()
                .subject(authentication.getName())
                .issuer(issuer)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .claim("role", "user")
                .signWith(getSigningKey()) 
                .compact();
    }

    public String extractUsername(String token) {
        return extractUsername(token, Claims::getSubject);
    }

    private <T> T extractUsername(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
    }

    public boolean validateToken(String token, UserDetails user) {
        final String username = extractUsername(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractUsername(token, Claims::getExpiration);
    }
}
