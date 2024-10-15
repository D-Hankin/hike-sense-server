package org.hikeSenseServer.models;

import java.util.List;

import org.bson.types.ObjectId;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

public class User extends PanacheMongoEntity {

    private ObjectId id; // Unique identifier for the user
    private String email; // User's email address
    private String password; // User's password (hashed)
    private String firstName; // User's first name
    private String lastName; // User's last name
    private List<Hike> hikes; // List of hikes associated with the user
    private List<ObjectId> friends; // List of friend IDs associated with the user

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
