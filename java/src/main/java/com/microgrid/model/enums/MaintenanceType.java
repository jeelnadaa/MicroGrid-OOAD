package com.microgrid.model.enums;

/**
 * Enum for maintenance types.
 * It follows SOLID single responsibility by representing only the allowed maintenance categories.
 * The enum itself is a simple creational mechanism for defining fixed instances.
 */

/**
 * Enum representing the types of maintenance events.
 * Maps to ENUM('CALIBRATION', 'REPAIR', 'REPLACEMENT') in the database.
 */
public enum MaintenanceType {
    CALIBRATION("CALIBRATION"),
    REPAIR("REPAIR"),
    REPLACEMENT("REPLACEMENT");

    private final String value;

    MaintenanceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MaintenanceType fromString(String text) {
        if (text == null) return null;
        for (MaintenanceType type : MaintenanceType.values()) {
            if (type.value.equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown maintenance type: " + text);
    }

    @Override
    public String toString() {
        return value;
    }
}
