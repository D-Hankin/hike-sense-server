package com.hikesenseserver.hikesenseserver.models;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Document(collection = "users")
public class User {

    @Id
    private ObjectId id; 

    @NotBlank(message = "Username cannot be blank")
    @Email(message = "Username must be a valid email")
    private String username; 

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    private List<Hike> hikes; 
    private List<ObjectId> friends; 

    public User() {
    }

    public User(ObjectId id, String username, String password, String firstName, String lastName, List<Hike> hikes, List<ObjectId> friends) {
        this.id = id;
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public void setHikes(ArrayList<Hike> arrayList) {
        this.hikes = arrayList;
    }

    public List<ObjectId> getFriends() {
        return friends;
    }

    public void setFriends(List<ObjectId> friends) {
        this.friends = friends;
    }

 
}
