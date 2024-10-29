package com.hikesenseserver.hikesenseserver.models;

public class SosMessage {
    private Location location;
    private String time;

    public SosMessage(Location location, String time) {
        this.location = location;
        this.time = time;
    }

    public Location getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }
}
