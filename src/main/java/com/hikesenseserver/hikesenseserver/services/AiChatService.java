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
        System.out.println("Chat Request: " + chatRequest);
        AiChatResponse response = restTemplate.postForObject(apiUrl, chatRequest, AiChatResponse.class);
        System.out.println("Chat Response: " + response.getChoices().get(0).getMessage().getContent());
        return response;
    }
    
}
