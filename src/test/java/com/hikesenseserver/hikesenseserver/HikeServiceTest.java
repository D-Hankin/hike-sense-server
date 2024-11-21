package com.hikesenseserver.hikesenseserver;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.hikesenseserver.hikesenseserver.models.Hike;
import com.hikesenseserver.hikesenseserver.models.User;
import com.hikesenseserver.hikesenseserver.repositories.UserRepository;
import com.hikesenseserver.hikesenseserver.services.HikeService;

@ExtendWith(MockitoExtension.class)
class HikeServiceTest {

    @InjectMocks
    private HikeService hikeService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetails userDetails;

    private User testUser;
    private Hike testHike;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setHikes(new ArrayList<>());
        testHike = new Hike();
        testHike.setName("Test Hike");
        testHike.setAvgHeartRate(80.0);
        testHike.setAlerts(new ArrayList<>());
        testHike.setDuration(120);
    }

    @Test
    void testNewHikeUserFoundSavesHike() {
        // Arrange
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(userRepository.findByUsername("user@example.com")).thenReturn(Optional.of(testUser));
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(userDetails, null));

        // Act
        ResponseEntity<String> response = hikeService.newHike(testHike);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("New hike saved!", response.getBody());
        assertEquals(1, testUser.getHikes().size());
        assertEquals(testHike.getName(), testUser.getHikes().get(0).getName());
    }

    @Test
    void testNewHikeUserNotFoundReturnsNotFound() {
        // Arrange
        when(userDetails.getUsername()).thenReturn("nonexistent@example.com");
        when(userRepository.findByUsername("nonexistent@example.com")).thenReturn(Optional.empty());
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(userDetails, null));

        // Act
        ResponseEntity<String> response = hikeService.newHike(testHike);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testNewHikeDataAccessExceptionReturnsInternalServerError() {
        // Arrange
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(userRepository.findByUsername("user@example.com")).thenThrow(new DataAccessException("Database error") {});
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(userDetails, null));

        // Act
        ResponseEntity<String> response = hikeService.newHike(testHike);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testFinishHikeHikeFoundUpdatesHike() {
        // Arrange
        testUser.getHikes().add(testHike);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(userRepository.findByUsername("user@example.com")).thenReturn(Optional.of(testUser));
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(userDetails, null));

        Hike updatedHike = new Hike();
        updatedHike.setName("Test Hike");
        updatedHike.setStartTime(new Date());
        updatedHike.setFinishTime(new Date());
        updatedHike.setAvgHeartRate(85.0);
        updatedHike.setDuration(150);
        updatedHike.setAlerts(new ArrayList<>());
        updatedHike.setFavorite(true);

        // Act
        ResponseEntity<String> response = hikeService.finishHike(updatedHike);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hike completed!", response.getBody());
        assertEquals(150, testUser.getHikes().get(0).getDuration());
        assertTrue(testUser.getHikes().get(0).isFavorite());
    }

    @Test
    void testFinishHikeHikeNotFoundReturnsNotFound() {
        // Arrange
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(userRepository.findByUsername("user@example.com")).thenReturn(Optional.of(testUser));
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(userDetails, null));

        Hike updatedHike = new Hike();
        updatedHike.setName("Nonexistent Hike");

        // Act
        ResponseEntity<String> response = hikeService.finishHike(updatedHike);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Hike not found.", response.getBody());
    }

    @Test
    void testFinishHikeDataAccessExceptionReturnsInternalServerError() {
        // Arrange
        testUser.getHikes().add(testHike);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(userRepository.findByUsername("user@example.com")).thenThrow(new DataAccessException("Database error") {});
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(userDetails, null));

        // Act
        ResponseEntity<String> response = hikeService.finishHike(testHike);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }
}
