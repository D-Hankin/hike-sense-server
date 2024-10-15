package com.hikesenseserver.hikesenseserver.models;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
    private ObjectId id; 
    private String email; 
    private String password;
    private String firstName; 
    private String lastName; 
    private List<Hike> hikes; 
    private List<ObjectId> friends; 

    public User() {
    }

    public User(ObjectId id, String email, String password, String firstName, String lastName, List<Hike> hikes, List<ObjectId> friends) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hikes = hikes;
        this.friends = friends;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Hike> getHikes() {
        return hikes;
    }

    public void setHikes(List<Hike> hikes) {
        this.hikes = hikes;
    }

    public List<ObjectId> getFriends() {
        return friends;
    }

    public void setFriends(List<ObjectId> friends) {
        this.friends = friends;
    }

 
}
