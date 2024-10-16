package com.hikesenseserver.hikesenseserver.models;

import java.util.Date;
import java.util.List;

public class Hike {
     private String name; // Name of the hike
    private Location startLocation; // Starting location coordinates (latitude and longitude)
    private Location finishLocation; // Finishing location coordinates (latitude and longitude)
    private Date startTime; // Start time of the hike
    private Date finishTime; // Finish time of the hike
    private double distance; // Distance of the hike
    private int duration; // Duration of the hike in minutes
    private String route; // Route information (could be a string or a more complex object)
    private boolean isFavorite; // Indicates if the hike is a favorite
    private double avgHeartRate; // Average heart rate during the hike
    private double avgTemp; // Average temperature during the hike
    private List<Alert> alerts; // List of alerts associated with the hike

    public Hike() {
    }

    public Hike(String name, Location startLocation, Location finishLocation, Date startTime, Date finishTime, double distance, int duration, String route, boolean isFavorite, double avgHeartRate, double avgTemp, List<Alert> alerts) {
        this.name = name;
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

}
