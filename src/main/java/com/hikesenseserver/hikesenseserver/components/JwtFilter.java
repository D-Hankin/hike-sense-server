package com.hikesenseserver.hikesenseserver.components;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hikesenseserver.hikesenseserver.repositories.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtFilter extends OncePerRequestFilter {
    
    @Autowired
    JwtComponent jwtCreationComponent;

    @Autowired
    ApplicationContext context;

    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtCreationComponent.extractUsername(token);
        } else {
            System.out.println("No JWT Token found in request.");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("Attempting to authenticate user: " + username);

            UserDetails user = context.getBean(UserRepository.class).findUserDetailsByUsername(username);
            if (user != null) {
                System.out.println("User details found for username: " + username);
            } else {
                System.out.println("No user details found for username: " + username);
            }

            if (jwtCreationComponent.validateToken(token, user)) {
                System.out.println("Token is valid. Authenticating user: " + username);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("User authenticated successfully: " + username);
            } else {
                System.out.println("Invalid token for user: " + username);
            }
        } else if (username != null) {
            System.out.println("User is already authenticated: " + username);
        } else {
            System.out.println("No username found in JWT token.");
        }

        filterChain.doFilter(request, response);
    }
}
