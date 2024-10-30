package com.hikesenseserver.hikesenseserver.components;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.hikesenseserver.hikesenseserver.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class WebsocketHandshakeInteceptor implements HandshakeInterceptor {

    @Autowired
    private JwtComponent jwtComponent;

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response, @NonNull WebSocketHandler wsHandler,
            @NonNull Map<String, Object> attributes) throws Exception {
        
        System.out.println("Before handshake");

        // Extract the HttpServletRequest from the current thread context
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            System.out.println("No request attributes found");
            return false;
        }
        HttpServletRequest servletRequest = requestAttributes.getRequest();
        
        String authHeader = servletRequest.getParameter("token");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            System.out.println("Token found in the request.");
            String token = authHeader.substring(7); // Remove "Bearer " prefix
            String username = jwtComponent.extractUsername(token); // Extract username from the token

            // Load user details from the database
            UserDetails userDetails = userRepository.findUserDetailsByUsername(username);

            if (userDetails != null && jwtComponent.validateToken(token, userDetails)) {
                // Token is valid, set Authentication in SecurityContext
                System.out.println("Token is valid. Authenticating user: " + username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return true;  // Proceed with the WebSocket handshake
            } else {
                System.out.println("Invalid token");
                return false;
            }
        } else {
            System.out.println("No token or invalid Authorization header");
            return false;
        }
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response, @NonNull WebSocketHandler wsHandler,
            @Nullable Exception exception) {
        // This method can be left unimplemented for now
        System.out.println("After handshake");
    }
}
