package com.microgrid.model;

/**
 * Domain model for locations.
 * It follows GRASP Information Expert by holding location state and related data.
 * It separates domain knowledge from persistence and request handling.
 */

import java.time.LocalDateTime;

/**
 * Location model — represents a geographical location where sensors are deployed.
 */
public class Location {
    private int locationId;
    private String areaName;
    private double latitude;
    private double longitude;
    private double elevation;
    private LocalDateTime createdAt;

    public Location() {}

    public Location(String areaName, double latitude, double longitude) {
        this.areaName = areaName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // --- Getters & Setters ---

    public int getLocationId() { return locationId; }
    public void setLocationId(int locationId) { this.locationId = locationId; }

    public String getAreaName() { return areaName; }
    public void setAreaName(String areaName) { this.areaName = areaName; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getElevation() { return elevation; }
    public void setElevation(double elevation) { this.elevation = elevation; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Location{locationId=" + locationId + ", areaName='" + areaName + "'}";
    }
}
