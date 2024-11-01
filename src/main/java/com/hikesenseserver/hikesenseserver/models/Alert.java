package com.hikesenseserver.hikesenseserver.models;

import java.time.LocalDateTime;

public class Alert {
    private String type;
    private String information;
    private Location location; 
    private LocalDateTime time; 

    public Alert() {
    }

    public Alert(String type, String information, Location location, LocalDateTime time) {
        this.type = type;
        this.information = information;
        this.location = location;
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String message) {
        this.information = message;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
