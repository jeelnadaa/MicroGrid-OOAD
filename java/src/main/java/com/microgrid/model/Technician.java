package com.microgrid.model;

/**
 * Domain model for technicians.
 * It follows GRASP Information Expert by holding technician attributes and behavior.
 * It keeps business rules separate from storage and HTTP handling.
 */

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Technician model — represents a maintenance technician.
 * Contains methods from the class diagram: performMaintenance(), addMaintenanceNotes().
 */
public class Technician {
    private int techId;
    private String name;
    private String contactNo;
    private String specialization;
    private LocalDateTime createdAt;

    // Aggregated maintenance records
    private List<MaintenanceRecord> maintenanceRecords = new ArrayList<>();

    public Technician() {}

    public Technician(String name, String contactNo, String specialization) {
        this.name = name;
        this.contactNo = contactNo;
        this.specialization = specialization;
    }

    // --- Domain methods from class diagram ---

    /**
     * Perform maintenance on a sensor — creates a new MaintenanceRecord.
     */
    public MaintenanceRecord performMaintenance(Sensor sensor, String eventType, String notes) {
        MaintenanceRecord record = new MaintenanceRecord();
        record.setSensorId(sensor.getSensorId());
        record.setTechId(this.techId);
        record.setEventType(eventType);
        record.setEventDate(LocalDateTime.now());
        record.setNotes(notes);
        this.maintenanceRecords.add(record);
        return record;
    }

    /**
     * Add notes to an existing maintenance record.
     */
    public void addMaintenanceNotes(MaintenanceRecord record, String additionalNotes) {
        String existing = record.getNotes() != null ? record.getNotes() + "\n" : "";
        record.setNotes(existing + additionalNotes);
    }

    // --- Getters & Setters ---

    public int getTechId() { return techId; }
    public void setTechId(int techId) { this.techId = techId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactNo() { return contactNo; }
    public void setContactNo(String contactNo) { this.contactNo = contactNo; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<MaintenanceRecord> getMaintenanceRecords() { return maintenanceRecords; }
    public void setMaintenanceRecords(List<MaintenanceRecord> maintenanceRecords) { this.maintenanceRecords = maintenanceRecords; }

    @Override
    public String toString() {
        return "Technician{techId=" + techId + ", name='" + name + "'}";
    }
}
