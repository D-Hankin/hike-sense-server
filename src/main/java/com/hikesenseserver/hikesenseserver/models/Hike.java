package com.hikesenseserver.hikesenseserver.models;

import java.util.Date;
import java.util.List;

public class Hike {

    private String id; 
    private String name; 
    private Date dateCreated;
    private Location startLocation;
    private Location finishLocation; 
    private Date startTime; 
    private Date finishTime; 
    private double distance; 
    private int duration; 
    private String route; 
    private boolean isFavorite; 
    private double avgHeartRate; 
    private double avgTemp; 
    private List<Alert> alerts; 
    private Boolean completed;

    public Hike() {
    }

    public Hike(String id, String name, Date dateCreated, Location startLocation, Location finishLocation, Date startTime, Date finishTime, double distance, int duration, String route, boolean isFavorite, double avgHeartRate, double avgTemp, List<Alert> alerts, Boolean completed) {
        this.id = id;
        this.name = name;
        this.dateCreated = dateCreated;
        this.startLocation = startLocation;
        this.finishLocation = finishLocation;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.distance = distance;
        this.duration = duration;
        this.route = route;
        this.isFavorite = isFavorite;
        this.avgHeartRate = avgHeartRate;
        this.avgTemp = avgTemp;
        this.alerts = alerts;
        this.completed = completed;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getFinishLocation() {
        return finishLocation;
    }

    public void setFinishLocation(Location finishLocation) {
        this.finishLocation = finishLocation;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public double getAvgHeartRate() {
        return avgHeartRate;
    }

    public void setAvgHeartRate(double avgHeartRate) {
        this.avgHeartRate = avgHeartRate;
    }

    public double getAvgTemp() {
        return avgTemp;
    }

    public void setAvgTemp(double avgTemp) {
        this.avgTemp = avgTemp;
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean isCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

}
