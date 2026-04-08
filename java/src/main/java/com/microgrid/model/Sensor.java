package com.microgrid.model;

/**
 * Domain model for sensors.
 * It follows GRASP Information Expert by owning sensor state and behavior.
 * It also follows SOLID encapsulation, keeping data private and exposing it
 * through methods rather than sharing internal state directly.
 */

import com.microgrid.model.enums.SensorStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Sensor model — represents an IoT sensor deployed in the grid.
 * Contains methods from the class diagram: updateStatus(), addReadings().
 */
public class Sensor {
    private int sensorId;
    private String model;
    private LocalDate installDate;
    private SensorStatus status;
    private int typeId;
    private int locationId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Transient display fields (populated by JOINs)
    private String sensorTypeName;
    private String locationName;

    // Aggregated relationships
    private List<Reading> readings = new ArrayList<>();
    private List<MaintenanceRecord> maintenanceRecords = new ArrayList<>();

    public Sensor() {
        this.status = SensorStatus.ACTIVE;
    }

    public Sensor(String model, LocalDate installDate, int typeId, int locationId) {
        this.model = model;
        this.installDate = installDate;
        this.typeId = typeId;
        this.locationId = locationId;
        this.status = SensorStatus.ACTIVE;
    }

    // --- Domain methods from class diagram ---

    /**
     * Update the status of this sensor.
     */
    public void updateStatus(SensorStatus newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Add a reading to this sensor's reading list.
     */
    public void addReadings(Reading reading) {
        reading.setSensorId(this.sensorId);
        this.readings.add(reading);
    }

    // --- Getters & Setters ---

    public int getSensorId() { return sensorId; }
    public void setSensorId(int sensorId) { this.sensorId = sensorId; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public LocalDate getInstallDate() { return installDate; }
    public void setInstallDate(LocalDate installDate) { this.installDate = installDate; }

    public SensorStatus getStatus() { return status; }
    public void setStatus(SensorStatus status) { this.status = status; }

    public int getTypeId() { return typeId; }
    public void setTypeId(int typeId) { this.typeId = typeId; }

    public int getLocationId() { return locationId; }
    public void setLocationId(int locationId) { this.locationId = locationId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getSensorTypeName() { return sensorTypeName; }
    public void setSensorTypeName(String sensorTypeName) { this.sensorTypeName = sensorTypeName; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }

    public List<Reading> getReadings() { return readings; }
    public void setReadings(List<Reading> readings) { this.readings = readings; }

    public List<MaintenanceRecord> getMaintenanceRecords() { return maintenanceRecords; }
    public void setMaintenanceRecords(List<MaintenanceRecord> maintenanceRecords) { this.maintenanceRecords = maintenanceRecords; }

    @Override
    public String toString() {
        return "Sensor{sensorId=" + sensorId + ", model='" + model + "', status=" + status + "}";
    }
}
