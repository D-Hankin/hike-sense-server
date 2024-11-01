package com.hikesenseserver.hikesenseserver;

import com.hikesenseserver.hikesenseserver.controllers.HikeController;
import com.hikesenseserver.hikesenseserver.models.Hike;
import com.hikesenseserver.hikesenseserver.services.HikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class HikeControllerTest {

    @InjectMocks
    private HikeController hikeController;

    @Mock
    private HikeService hikeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void newHikeShouldReturnCreatedResponse() {
        // Arrange
        Hike hike = new Hike();
        hike.setName("Test Hike");
        hike.setAvgHeartRate(80.0);
        hike.setDuration(120);
        
        when(hikeService.newHike(any(Hike.class)))
            .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("New hike saved!"));

        // Act
        ResponseEntity<String> response = hikeController.newHike(hike);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("New hike saved!", response.getBody());
    }

    @Test
    public void finishHikeShouldReturnOkResponse() {
        // Arrange
        Hike hike = new Hike();
        hike.setName("Test Hike");
        
        when(hikeService.finishHike(any(Hike.class)))
            .thenReturn(ResponseEntity.ok("Hike completed!"));

        // Act
        ResponseEntity<String> response = hikeController.finishHike(hike);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hike completed!", response.getBody());
    }

    @Test
    public void newHikeUserNotFoundShouldReturnNotFound() {
        // Arrange
        Hike hike = new Hike();
        hike.setName("Test Hike");
        
        when(hikeService.newHike(any(Hike.class)))
            .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));

        // Act
        ResponseEntity<String> response = hikeController.newHike(hike);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void finishHikeNotFoundShouldReturnNotFound() {
        // Arrange
        Hike hike = new Hike();
        hike.setName("Nonexistent Hike");
        
        when(hikeService.finishHike(any(Hike.class)))
            .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hike not found."));

        // Act
        ResponseEntity<String> response = hikeController.finishHike(hike);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Hike not found.", response.getBody());
    }
}
