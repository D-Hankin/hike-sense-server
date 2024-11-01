package com.hikesenseserver.hikesenseserver;

import com.hikesenseserver.hikesenseserver.models.AiChatRequest;
import com.hikesenseserver.hikesenseserver.models.AiChatResponse;
import com.hikesenseserver.hikesenseserver.services.AiChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.ArrayList;

public class AiChatServiceTest {

    @InjectMocks
    private AiChatService aiChatService;

    @Mock
    private RestTemplate restTemplate;

    private static final String apiUrl = "http://api.example.com/chat";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        aiChatService = new AiChatService(restTemplate, apiUrl);
    }

    @Test
    public void sendChatResponseShouldReturnValidResponse() {
        // Arrange
        String prompt = "What should I pack for a day hike?";
        AiChatResponse aiChatResponse = new AiChatResponse();
        aiChatResponse.setChoices(new ArrayList<>(List.of(new AiChatResponse.Choice())));

        when(restTemplate.postForObject(eq(apiUrl), any(AiChatRequest.class), eq(AiChatResponse.class))).thenReturn(aiChatResponse);

        // Act
        AiChatResponse response = aiChatService.sendChatResponse(prompt);

        // Assert
        assertNotNull(response);
        verify(restTemplate, times(1)).postForObject(eq(apiUrl), any(AiChatRequest.class), eq(AiChatResponse.class));
    }

    @Test
    public void sendChatResponseShouldHandleNullResponse() {
        // Arrange
        String prompt = "What are the best hiking spots?";
        
        when(restTemplate.postForObject(eq(apiUrl), any(AiChatRequest.class), eq(AiChatResponse.class))).thenReturn(null);

        // Act
        AiChatResponse response = aiChatService.sendChatResponse(prompt);

        // Assert
        assertNotNull(response); 
    }
}
