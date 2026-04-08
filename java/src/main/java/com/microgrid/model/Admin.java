package com.microgrid.model;

/**
 * Domain model for admin users.
 * It follows GRASP Information Expert by representing admin-specific state.
 * It keeps admin behavior separate from authentication and persistence logic.
 */

import com.microgrid.model.enums.SensorStatus;

/**
 * Admin class — extends User with additional sensor grid management capabilities.
 * Corresponds to the Admin actor in the class diagram.
 */
public class Admin extends User {

    public Admin() {
        setRole("admin");
    }

    public Admin(String username, String email, String fullName) {
        super(username, email, fullName, "admin");
    }

    /**
     * Add a new sensor type to the system.
     */
    public SensorType addSensorType(String name, String description) {
        SensorType sensorType = new SensorType();
        sensorType.setName(name);
        sensorType.setDescription(description);
        return sensorType;
    }

    /**
     * Add a new location to the system.
     */
    public Location addLocation(String areaName, double latitude, double longitude) {
        Location location = new Location();
        location.setAreaName(areaName);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

    /**
     * Register a new sensor in the system.
     */
    public Sensor registerSensor(String model, int typeId, int locationId) {
        Sensor sensor = new Sensor();
        sensor.setModel(model);
        sensor.setTypeId(typeId);
        sensor.setLocationId(locationId);
        sensor.setStatus(SensorStatus.ACTIVE);
        return sensor;
    }

    /**
     * Manage sensors — update sensor status or configuration.
     */
    public void manageSensors(Sensor sensor, SensorStatus newStatus) {
        sensor.updateStatus(newStatus);
    }

    @Override
    public String toString() {
        return "Admin{userId=" + getUserId() + ", username='" + getUsername() + "'}";
    }
}
