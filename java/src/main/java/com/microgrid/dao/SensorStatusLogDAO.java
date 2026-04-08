package com.microgrid.dao;

/**
 * This DAO handles persistence for sensor status log entries.
 * It follows SOLID single responsibility by limiting itself to data access.
 * It also applies GRASP Information Expert by knowing how to map ResultSet
 * rows into SensorStatusLog objects.
 */

import com.microgrid.model.SensorStatusLog;
import com.microgrid.model.enums.SensorStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for SensorStatusLog entity.
 */
public class SensorStatusLogDAO {

    public List<SensorStatusLog> findRecent(int limit) throws SQLException {
        String sql = """
            SELECT ssl.*, s.model AS sensor_model
            FROM SensorStatusLog ssl
            JOIN Sensor s ON ssl.sensor_id = s.sensor_id
            ORDER BY ssl.change_timestamp DESC
            LIMIT ?
            """;
        List<SensorStatusLog> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<SensorStatusLog> findBySensorId(int sensorId) throws SQLException {
        String sql = """
            SELECT ssl.*, s.model AS sensor_model
            FROM SensorStatusLog ssl
            JOIN Sensor s ON ssl.sensor_id = s.sensor_id
            WHERE ssl.sensor_id = ?
            ORDER BY ssl.change_timestamp DESC
            """;
        List<SensorStatusLog> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sensorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    private SensorStatusLog mapRow(ResultSet rs) throws SQLException {
        SensorStatusLog log = new SensorStatusLog();
        log.setLogId(rs.getInt("log_id"));
        log.setSensorId(rs.getInt("sensor_id"));
        String oldStatus = rs.getString("old_status");
        if (oldStatus != null) log.setOldStatus(SensorStatus.fromString(oldStatus));
        String newStatus = rs.getString("new_status");
        if (newStatus != null) log.setNewStatus(SensorStatus.fromString(newStatus));
        Timestamp ts = rs.getTimestamp("change_timestamp");
        if (ts != null) log.setChangeTimestamp(ts.toLocalDateTime());
        try { log.setSensorModel(rs.getString("sensor_model")); } catch (SQLException ignored) {}
        return log;
    }
}
