package com.microgrid.model.enums;

/**
 * Enum for sensor statuses.
 * It follows SOLID single responsibility by representing only status values.
 * The enum is also a creational convenience, providing a fixed set of instances.
 */

/**
 * Enum representing the possible statuses of a sensor.
 * Maps to ENUM('ACTIVE', 'INACTIVE', 'MAINTENANCE') in the database.
 */
public enum SensorStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    MAINTENANCE("MAINTENANCE");

    private final String value;

    SensorStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SensorStatus fromString(String text) {
        if (text == null) return null;
        for (SensorStatus status : SensorStatus.values()) {
            if (status.value.equalsIgnoreCase(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown sensor status: " + text);
    }

    @Override
    public String toString() {
        return value;
    }
}
