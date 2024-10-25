package com.hikesenseserver.hikesenseserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hikesenseserver.hikesenseserver.models.AiChatResponse;
import com.hikesenseserver.hikesenseserver.services.AiChatService;

@RestController
@RequestMapping("/hike-buddy")
public class AiChatController {
    
    @Autowired
    AiChatService aiChatService;
    
    @PostMapping("/chat")
    public AiChatResponse postChat(@RequestBody String promt) {
        return aiChatService.sendChatResponse(promt);
    }
}
