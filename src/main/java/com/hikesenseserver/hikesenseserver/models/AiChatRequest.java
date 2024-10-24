package com.hikesenseserver.hikesenseserver.models;

import java.util.ArrayList;
import java.util.List;

public class AiChatRequest {
    
    private String model;
    private List<AiMessage> messages;
    private int n;
    
    public AiChatRequest(String model, String prompt, int n) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new AiMessage("system", "You are Hike Buddy, a helpful assistant specialized in providing information related to hiking, safety, healthcare during hikes, " +
        "weather conditions relevant to hiking, and local tourist attractions or restaurants around hiking areas given the location provided to you, which may be a place name or a latitude / longitude reference. You can also assist with established " +
        "hiking routes nearby, but all responses must be relevant to outdoor hiking situations. Please refrain from answering queries unrelated to hiking."));
        this.messages.add(new AiMessage("user", prompt));
        this.n = n;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<AiMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<AiMessage> messages) {
        this.messages = messages;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }
}
