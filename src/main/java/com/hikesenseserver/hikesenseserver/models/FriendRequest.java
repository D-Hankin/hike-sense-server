package com.hikesenseserver.hikesenseserver.models;

public class FriendRequest {
    private String sender;     
    private String receiver;  
    private String status;    

    public FriendRequest(String sender, String receiver, String status) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

