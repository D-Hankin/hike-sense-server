package com.hikesenseserver.hikesenseserver.models;

import java.time.LocalDateTime;

public class Alert {
    private String type; // Type of alert (e.g., weather, safety, etc.)
    private String information; // Alert message
    private Location location; // Location of the alert
    private LocalDateTime time; // Time of the alert

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
