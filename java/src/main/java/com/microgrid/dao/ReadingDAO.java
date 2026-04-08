package com.microgrid.dao;

/**
 * This DAO owns reading persistence and maps database rows to Reading objects.
 * It follows GRASP Information Expert by keeping the data access knowledge
 * for readings in one place, and SOLID Single Responsibility by avoiding
 * unrelated logic.
 */

import com.microgrid.model.Reading;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Reading entity.
 */
public class ReadingDAO {

    public Reading findById(long readingId) throws SQLException {
        String sql = """
            SELECT r.*, s.model AS sensor_model, st.name AS sensor_type_name, l.area_name AS location_name
            FROM Reading r
            JOIN Sensor s ON r.sensor_id = s.sensor_id
            JOIN SensorType st ON s.type_id = st.type_id
            JOIN Location l ON s.location_id = l.location_id
            WHERE r.reading_id = ?
            """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, readingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        }
        return null;
    }

    public List<Reading> findAll(int limit, int offset) throws SQLException {
        String sql = """
            SELECT r.*, s.model AS sensor_model, st.name AS sensor_type_name, l.area_name AS location_name
            FROM Reading r
            JOIN Sensor s ON r.sensor_id = s.sensor_id
            JOIN SensorType st ON s.type_id = st.type_id
            JOIN Location l ON s.location_id = l.location_id
            ORDER BY r.reading_timestamp DESC
            LIMIT ? OFFSET ?
            """;
        List<Reading> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Reading> findBySensorId(int sensorId) throws SQLException {
        String sql = """
            SELECT r.*, s.model AS sensor_model, st.name AS sensor_type_name, l.area_name AS location_name
            FROM Reading r
            JOIN Sensor s ON r.sensor_id = s.sensor_id
            JOIN SensorType st ON s.type_id = st.type_id
            JOIN Location l ON s.location_id = l.location_id
            WHERE r.sensor_id = ?
            ORDER BY r.reading_timestamp DESC
            """;
        List<Reading> list = new ArrayList<>();
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

    /**
     * Call stored procedure GetSensorReadings.
     */
    public List<Reading> getSensorReadings(int sensorId) throws SQLException {
        String sql = "{CALL GetSensorReadings(?)}";
        List<Reading> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, sensorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Reading r = new Reading();
                r.setReadingId(rs.getLong("reading_id"));
                r.setSensorId(rs.getInt("sensor_id"));
                r.setSensorModel(rs.getString("model"));
                r.setSensorTypeName(rs.getString("sensor_type"));
                r.setReadingValue(rs.getBigDecimal("reading_value"));
                Timestamp ts = rs.getTimestamp("reading_timestamp");
                if (ts != null) r.setReadingTimestamp(ts.toLocalDateTime());
                list.add(r);
            }
        }
        return list;
    }

    public void save(Reading reading) throws SQLException {
        String sql = "INSERT INTO Reading (sensor_id, reading_value, reading_timestamp) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, reading.getSensorId());
            stmt.setBigDecimal(2, reading.getReadingValue());
            stmt.setTimestamp(3, Timestamp.valueOf(reading.getReadingTimestamp()));
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                reading.setReadingId(keys.getLong(1));
            }
        }
    }

    public void update(Reading reading) throws SQLException {
        String sql = "UPDATE Reading SET sensor_id = ?, reading_value = ?, reading_timestamp = ? WHERE reading_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reading.getSensorId());
            stmt.setBigDecimal(2, reading.getReadingValue());
            stmt.setTimestamp(3, Timestamp.valueOf(reading.getReadingTimestamp()));
            stmt.setLong(4, reading.getReadingId());
            stmt.executeUpdate();
        }
    }

    public void delete(long readingId) throws SQLException {
        String sql = "DELETE FROM Reading WHERE reading_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, readingId);
            stmt.executeUpdate();
        }
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Reading";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    /**
     * Get recent readings with joined details.
     */
    public List<Reading> findRecent(int limit) throws SQLException {
        return findAll(limit, 0);
    }

    /**
     * Get average readings by sensor type.
     */
    public List<Object[]> getAverageReadingsBySensorType() throws SQLException {
        String sql = """
            SELECT st.name, AVG(r.reading_value) AS avg_value, COUNT(r.reading_id) AS reading_count
            FROM SensorType st
            JOIN Sensor s ON st.type_id = s.type_id
            JOIN Reading r ON s.sensor_id = r.sensor_id
            GROUP BY st.name
            """;
        List<Object[]> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("name"),
                    rs.getBigDecimal("avg_value"),
                    rs.getInt("reading_count")
                });
            }
        }
        return list;
    }

    private Reading mapRow(ResultSet rs) throws SQLException {
        Reading r = new Reading();
        r.setReadingId(rs.getLong("reading_id"));
        r.setSensorId(rs.getInt("sensor_id"));
        r.setReadingValue(rs.getBigDecimal("reading_value"));
        Timestamp ts = rs.getTimestamp("reading_timestamp");
        if (ts != null) r.setReadingTimestamp(ts.toLocalDateTime());
        try { r.setSensorModel(rs.getString("sensor_model")); } catch (SQLException ignored) {}
        try { r.setSensorTypeName(rs.getString("sensor_type_name")); } catch (SQLException ignored) {}
        try { r.setLocationName(rs.getString("location_name")); } catch (SQLException ignored) {}
        return r;
    }
}
