package com.microgrid.model;

/**
 * Domain model for maintenance records.
 * It follows GRASP Information Expert by encapsulating maintenance details.
 * It also obeys SOLID single responsibility by representing only maintenance state.
 */

import java.time.LocalDateTime;

/**
 * MaintenanceRecord model — stores maintenance event data for sensors.
 * Named MaintenanceRecord (as per class diagram) — maps to MaintenanceEvent table.
 */
public class MaintenanceRecord {
    private int maintenanceId;
    private int sensorId;
    private int techId;
    private String eventType;       // CALIBRATION, REPAIR, REPLACEMENT
    private LocalDateTime eventDate;
    private String notes;
    private LocalDateTime createdAt;

    // Transient display fields
    private String sensorModel;
    private String technicianName;

    public MaintenanceRecord() {}

    public MaintenanceRecord(int sensorId, int techId, String eventType, LocalDateTime eventDate, String notes) {
        this.sensorId = sensorId;
        this.techId = techId;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.notes = notes;
    }

    // --- Getters & Setters ---

    public int getMaintenanceId() { return maintenanceId; }
    public void setMaintenanceId(int maintenanceId) { this.maintenanceId = maintenanceId; }

    public int getSensorId() { return sensorId; }
    public void setSensorId(int sensorId) { this.sensorId = sensorId; }

    public int getTechId() { return techId; }
    public void setTechId(int techId) { this.techId = techId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public LocalDateTime getEventDate() { return eventDate; }
    public void setEventDate(LocalDateTime eventDate) { this.eventDate = eventDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getSensorModel() { return sensorModel; }
    public void setSensorModel(String sensorModel) { this.sensorModel = sensorModel; }

    public String getTechnicianName() { return technicianName; }
    public void setTechnicianName(String technicianName) { this.technicianName = technicianName; }

    @Override
    public String toString() {
        return "MaintenanceRecord{maintenanceId=" + maintenanceId +
               ", eventType='" + eventType + "'}";
    }
}
