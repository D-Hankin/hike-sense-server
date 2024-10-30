package com.hikesenseserver.hikesenseserver.models;

public class NotificationResponse {
    
    private String recipient;
    private String response;

    public NotificationResponse() {
    }

    public NotificationResponse(String recipient, String response) {
        this.recipient = recipient;
        this.response = response;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
