package com.hikesenseserver.hikesenseserver.models;

public class SosMessage {
    private String location;
    private String time;

    public SosMessage(String location, String time) {
        this.location = location;
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }
}
