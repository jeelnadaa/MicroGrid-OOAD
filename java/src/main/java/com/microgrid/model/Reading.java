package com.microgrid.model;

/**
 * Domain model for sensor readings.
 * It follows GRASP Information Expert by owning reading data and accessors.
 * This class keeps domain state separate from persistence and controller logic.
 */

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Reading model — stores sensor measurement data.
 */
public class Reading {
    private long readingId;
    private int sensorId;
    private BigDecimal readingValue;
    private LocalDateTime readingTimestamp;

    // Transient display fields
    private String sensorModel;
    private String sensorTypeName;
    private String locationName;

    public Reading() {}

    public Reading(int sensorId, BigDecimal readingValue, LocalDateTime readingTimestamp) {
        this.sensorId = sensorId;
        this.readingValue = readingValue;
        this.readingTimestamp = readingTimestamp;
    }

    // --- Getters & Setters ---

    public long getReadingId() { return readingId; }
    public void setReadingId(long readingId) { this.readingId = readingId; }

    public int getSensorId() { return sensorId; }
    public void setSensorId(int sensorId) { this.sensorId = sensorId; }

    public BigDecimal getReadingValue() { return readingValue; }
    public void setReadingValue(BigDecimal readingValue) { this.readingValue = readingValue; }

    public LocalDateTime getReadingTimestamp() { return readingTimestamp; }
    public void setReadingTimestamp(LocalDateTime readingTimestamp) { this.readingTimestamp = readingTimestamp; }

    public String getSensorModel() { return sensorModel; }
    public void setSensorModel(String sensorModel) { this.sensorModel = sensorModel; }

    public String getSensorTypeName() { return sensorTypeName; }
    public void setSensorTypeName(String sensorTypeName) { this.sensorTypeName = sensorTypeName; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }

    @Override
    public String toString() {
        return "Reading{readingId=" + readingId + ", sensorId=" + sensorId +
               ", value=" + readingValue + "}";
    }
}
