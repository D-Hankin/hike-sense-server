package com.hikesenseserver.hikesenseserver;

import com.hikesenseserver.hikesenseserver.controllers.AiChatController;
import com.hikesenseserver.hikesenseserver.models.AiChatResponse;
import com.hikesenseserver.hikesenseserver.services.AiChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AiControllerTest {

    @InjectMocks
    private AiChatController aiChatController;

    @Mock
    private AiChatService aiChatService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void postChatShouldReturnChatResponse() {
        // Arrange
        String prompt = "Tell me about the best hiking trails.";
        AiChatResponse aiChatResponse = new AiChatResponse();
        
        when(aiChatService.sendChatResponse(prompt)).thenReturn(aiChatResponse);

        // Act
        AiChatResponse response = aiChatController.postChat(prompt);

        // Assert
        assertEquals(aiChatResponse, response);
        verify(aiChatService, times(1)).sendChatResponse(prompt);
    }
}
