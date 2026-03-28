package com.microgrid.model;

/**
 * Domain model for sensor type metadata.
 * It follows GRASP Information Expert by owning sensor type state and names.
 * It also follows Single Responsibility by representing only sensor type data.
 */

import java.time.LocalDateTime;

/**
 * SensorType model — defines the type/category of a sensor.
 */
public class SensorType {
    private int typeId;
    private String name;
    private String description;
    private LocalDateTime createdAt;

    public SensorType() {}

    public SensorType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // --- Getters & Setters ---

    public int getTypeId() { return typeId; }
    public void setTypeId(int typeId) { this.typeId = typeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "SensorType{typeId=" + typeId + ", name='" + name + "'}";
    }
}
