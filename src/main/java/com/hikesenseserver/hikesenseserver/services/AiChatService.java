package com.hikesenseserver.hikesenseserver.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hikesenseserver.hikesenseserver.models.AiChatRequest;
import com.hikesenseserver.hikesenseserver.models.AiChatResponse;

@Service
public class AiChatService {

    @Value("${AI_API_URL}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public AiChatService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AiChatResponse sendChatResponse(String prompt) {
        AiChatRequest chatRequest = new AiChatRequest("gpt-4o", prompt, 1);
        AiChatResponse response = restTemplate.postForObject(apiUrl, chatRequest, AiChatResponse.class);
        if (response != null && response.getChoices() != null && !response.getChoices().isEmpty() && response.getChoices().get(0).getMessage() != null) {
        } else {
            System.out.println("Chat Response: No valid response received.");
        }
        return response;
    }
    
}
