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
    private List<Friend> friends; 
    private List<String> pendingFriendRequests;
    private String subscriptionStatus;
    private List<String> authorities;

    public User() {
    }

    public User(ObjectId id, String username, String password, String firstName, String lastName, List<Hike> hikes, List<Friend> friends, List<String> pendingFriendRequests, String subscriptionStatus, List<String> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hikes = hikes != null ? hikes : new ArrayList<>();
        this.friends = friends != null ? friends : new ArrayList<>();
        this.pendingFriendRequests = pendingFriendRequests != null ? pendingFriendRequests : new ArrayList<>();
        this.subscriptionStatus = subscriptionStatus;
        this.authorities = authorities;
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

    public void setHikes(List<Hike> hikes) {
        this.hikes = hikes;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> role) {
        this.authorities = role;
    }

    public List<String> getPendingFriendRequests() {
        return pendingFriendRequests;
    }

    public void setPendingFriendRequests(List<String> friendRequests) {
        this.pendingFriendRequests = friendRequests;
    }

 
}
