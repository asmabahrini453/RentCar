package com.stage.rentcar.api;

public class Location {
    private String displayName;
    private double lat;
    private double lon;

    public Location(String displayName, double lat, double lon) {
        this.displayName = displayName;
        this.lat = lat;
        this.lon = lon;
    }

    // Getters and setters
    public String getDisplayName() {
        return displayName;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

}
