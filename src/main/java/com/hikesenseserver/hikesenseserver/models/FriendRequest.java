package com.hikesenseserver.hikesenseserver.models;

public class FriendRequest {
    private String sender;     // Username of the user sending the request
    private String receiver;   // Username of the user receiving the request
    // private LocalDateTime timestamp; // When the request was sent
    private String status;     // Status of the request (e.g., "PENDING", "ACCEPTED", "DECLINED")

    // // Constructors
    // public FriendRequest() {
    //     this.timestamp = LocalDateTime.now(); // Set the timestamp to the current time by default
    //     this.status = "PENDING"; // Default status
    // }

    public FriendRequest(String sender, String receiver, String status) {
        this.sender = sender;
        this.receiver = receiver;
        // this.timestamp = LocalDateTime.now();
        this.status = status;
    }

    // Getters and Setters
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

    // public LocalDateTime getTimestamp() {
    //     return timestamp;
    // }

    // public void setTimestamp(LocalDateTime timestamp) {
    //     this.timestamp = timestamp;
    // }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // @Override
    // public String toString() {
    //     return "FriendRequest{" +
    //             "sender='" + sender + '\'' +
    //             ", receiver='" + receiver + '\'' +
    //             ", timestamp=" + timestamp +
    //             ", status='" + status + '\'' +
    //             '}';
    // }
}

