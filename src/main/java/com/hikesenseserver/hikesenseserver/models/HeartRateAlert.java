package com.hikesenseserver.hikesenseserver.models;

public class HeartRateAlert {
    
    private Location location;
    private String message;
    private String time;

    public HeartRateAlert(Location location, String message, String time) {
        this.location = location;
        this.message = message;
        this.time = time;
    }

    public Location getLocation() {
        return location;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }
}
