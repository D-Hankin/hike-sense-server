package com.hikesenseserver.hikesenseserver.models;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class Friend {
    
    @NotBlank(message = "Username cannot be blank")
    @Email(message = "Username must be a valid email")
    private String usernameFriend; 

    @NotBlank(message = "First name cannot be blank")
    private String firstNameFriend;

    @NotBlank(message = "Last name cannot be blank")
    private String lastNameFriend;

    private List<Hike> hikesFriend; 

    public Friend() {
    }

    public Friend(String usernameFriend, String firstNameFriend, String lastNameFriend, List<Hike> hikesFriend) {
        this.usernameFriend = usernameFriend;
        this.firstNameFriend = firstNameFriend;
        this.lastNameFriend = lastNameFriend;
        this.hikesFriend = hikesFriend;
    }

	public String getUsernameFriend() {
		return usernameFriend;
	}

	public void setUsernameFriend(String usernameFriend) {
		this.usernameFriend = usernameFriend;
	}

	public String getFirstNameFriend() {
		return firstNameFriend;
	}

	public void setFirstNameFriend(String firstNameFriend) {
		this.firstNameFriend = firstNameFriend;
	}

	public String getLastNameFriend() {
		return lastNameFriend;
	}

	public void setLastNameFriend(String lastNameFriend) {
		this.lastNameFriend = lastNameFriend;
	}

	public List<Hike> getHikesFriend() {
		return hikesFriend;
	}

	public void setHikesFriend(List<Hike> hikesFriend) {
		this.hikesFriend = hikesFriend;
	}

    
    

}
