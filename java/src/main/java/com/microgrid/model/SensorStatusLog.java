package com.microgrid.model;

/**
 * Domain model for sensor status logs.
 * It follows GRASP Information Expert by encapsulating audit details for status changes.
 * It keeps its own state and does not mix in persistence responsibilities.
 */

import com.microgrid.model.enums.SensorStatus;

import java.time.LocalDateTime;

/**
 * SensorStatusLog model — logs sensor status changes (populated by DB trigger).
 */
public class SensorStatusLog {
    private int logId;
    private int sensorId;
    private SensorStatus oldStatus;
    private SensorStatus newStatus;
    private LocalDateTime changeTimestamp;

    // Transient display fields
    private String sensorModel;

    public SensorStatusLog() {}

    // --- Getters & Setters ---

    public int getLogId() { return logId; }
    public void setLogId(int logId) { this.logId = logId; }

    public int getSensorId() { return sensorId; }
    public void setSensorId(int sensorId) { this.sensorId = sensorId; }

    public SensorStatus getOldStatus() { return oldStatus; }
    public void setOldStatus(SensorStatus oldStatus) { this.oldStatus = oldStatus; }

    public SensorStatus getNewStatus() { return newStatus; }
    public void setNewStatus(SensorStatus newStatus) { this.newStatus = newStatus; }

    public LocalDateTime getChangeTimestamp() { return changeTimestamp; }
    public void setChangeTimestamp(LocalDateTime changeTimestamp) { this.changeTimestamp = changeTimestamp; }

    public String getSensorModel() { return sensorModel; }
    public void setSensorModel(String sensorModel) { this.sensorModel = sensorModel; }

    @Override
    public String toString() {
        return "SensorStatusLog{logId=" + logId + ", sensorId=" + sensorId +
               ", " + oldStatus + " -> " + newStatus + "}";
    }
}
