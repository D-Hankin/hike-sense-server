package com.hikesenseserver.hikesenseserver;

import com.hikesenseserver.hikesenseserver.config.EmailConfig;
import com.hikesenseserver.hikesenseserver.models.Friend;
import com.hikesenseserver.hikesenseserver.models.HeartRateAlert;
import com.hikesenseserver.hikesenseserver.models.Location;
import com.hikesenseserver.hikesenseserver.models.User;
import com.hikesenseserver.hikesenseserver.repositories.UserRepository;
import com.hikesenseserver.hikesenseserver.services.AlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AlertServiceTest {

    @InjectMocks
    private AlertService alertService;

    @Mock
    private EmailConfig emailConfig;

    @Mock
    private UserRepository userRepository;

    @Mock
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(new org.springframework.security.core.userdetails.User("testUser", "", new ArrayList<>()));
        when(emailConfig.getEmailHost()).thenReturn("smtp.example.com");
        when(emailConfig.getEmailPort()).thenReturn("587");
        when(emailConfig.getEmailUsername()).thenReturn("test@example.com");
        when(emailConfig.getEmailPassword()).thenReturn("password");
    }

    @Test
    public void sendAlertShouldSendEmailToFriends() throws MessagingException {
        // Arrange
        Location location = new Location(12.34, 56.78);
        HeartRateAlert alert = new HeartRateAlert(location, "High heart rate detected!", "2024-11-01T12:00:00Z");
        Friend friend = new Friend("friend@example.com", "FriendFirstName", "FriendLastName", new ArrayList<>());

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(user.getFriends()).thenReturn(Collections.singletonList(friend));

        // Act
        ResponseEntity<String> response = alertService.sendAlert(alert);

        // Assert
        assertEquals(ResponseEntity.ok("Alert sent"), response);
        verify(userRepository, times(1)).findByUsername("testUser");
        verify(user, times(1)).getFriends();
    }

    @Test
    public void sendAlertShouldHandleNoFriends() throws MessagingException {
        // Arrange
        Location location = new Location(12.34, 56.78);
        HeartRateAlert alert = new HeartRateAlert(location, "High heart rate detected!", "2024-11-01T12:00:00Z");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(user.getFriends()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<String> response = alertService.sendAlert(alert);

        // Assert
        assertEquals(ResponseEntity.ok("Alert sent"), response);
        verify(userRepository, times(1)).findByUsername("testUser");
        verify(user, times(1)).getFriends();
    }

    @Test
    public void sendAlertShouldReturnBadRequestOnEmailFailure() throws Exception {
        // Arrange
        Location location = new Location(12.34, 56.78);
        HeartRateAlert alert = new HeartRateAlert(location, "High heart rate detected!", "2024-11-01T12:00:00Z");
        Friend friend = new Friend("friend@example.com", "FriendFirstName", "FriendLastName", new ArrayList<>());

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(user.getFriends()).thenReturn(Collections.singletonList(friend));

        // Act
        ResponseEntity<String> response = alertService.sendAlert(alert);

        // Assert
        assertEquals(ResponseEntity.badRequest().body("Email not sent"), response);
        verify(userRepository, times(1)).findByUsername("testUser");
        verify(user, times(1)).getFriends();
    }
}
