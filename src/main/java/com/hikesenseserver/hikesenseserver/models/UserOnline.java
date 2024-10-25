package com.hikesenseserver.hikesenseserver.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usersOnline")
public class UserOnline {
    
    @Id
    private String id;
    private String username;

    public UserOnline() {
    }

    public UserOnline(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public UserOnline(String username2) {
        this.username = username2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
