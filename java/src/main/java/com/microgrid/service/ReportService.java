package com.microgrid.service;

/**
 * This service aggregates data for reports and keeps reporting rules in one place.
 * It follows GRASP Controller by coordinating data retrieval from multiple DAOs.
 * It also follows Single Responsibility by separating report assembly from
 * raw persistence operations.
 */

import com.microgrid.dao.SensorStatusLogDAO;
import com.microgrid.model.SensorStatusLog;

import java.sql.SQLException;
import java.util.List;

/**
 * Service layer for reports and analytics — aggregates data from multiple DAOs.
 * Corresponds to the SystemUsing interface (viewSensorReadings) in the class diagram.
 */
public class ReportService {

    private final SensorStatusLogDAO statusLogDAO = new SensorStatusLogDAO();

    /**
     * Get recent status change logs.
     */
    public List<SensorStatusLog> getRecentStatusLogs(int limit) throws SQLException {
        return statusLogDAO.findRecent(limit);
    }

    /**
     * View sensor readings — delegates to ReadingService.
     * This method fulfills the SystemUsing.viewSensorReadings() interface from the class diagram.
     */
    public List<SensorStatusLog> getStatusLogsBySensor(int sensorId) throws SQLException {
        return statusLogDAO.findBySensorId(sensorId);
    }
}
