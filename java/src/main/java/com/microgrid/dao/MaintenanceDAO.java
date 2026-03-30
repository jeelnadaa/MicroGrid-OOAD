package com.microgrid.dao;

/**
 * This DAO manages maintenance record persistence.
 * It follows GRASP Information Expert by handling the SQL details for
 * maintenance data. It also follows SOLID single responsibility by
 * leaving business decisions to the service layer.
 */

import com.microgrid.model.MaintenanceRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for MaintenanceRecord (MaintenanceEvent table).
 */
public class MaintenanceDAO {

    public MaintenanceRecord findById(int maintenanceId) throws SQLException {
        String sql = """
            SELECT m.*, s.model AS sensor_model, t.name AS technician_name
            FROM MaintenanceEvent m
            JOIN Sensor s ON m.sensor_id = s.sensor_id
            JOIN Technician t ON m.tech_id = t.tech_id
            WHERE m.maintenance_id = ?
            """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, maintenanceId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        }
        return null;
    }

    public List<MaintenanceRecord> findAll() throws SQLException {
        String sql = """
            SELECT m.*, s.model AS sensor_model, t.name AS technician_name
            FROM MaintenanceEvent m
            JOIN Sensor s ON m.sensor_id = s.sensor_id
            JOIN Technician t ON m.tech_id = t.tech_id
            ORDER BY m.event_date DESC
            """;
        return executeQuery(sql);
    }

    public List<MaintenanceRecord> findFiltered(String sensorId, String techId, String eventType) throws SQLException {
        StringBuilder sql = new StringBuilder("""
            SELECT m.*, s.model AS sensor_model, t.name AS technician_name
            FROM MaintenanceEvent m
            JOIN Sensor s ON m.sensor_id = s.sensor_id
            JOIN Technician t ON m.tech_id = t.tech_id
            WHERE 1=1
            """);
        List<Object> params = new ArrayList<>();

        if (sensorId != null && !sensorId.isBlank()) {
            sql.append(" AND m.sensor_id = ?");
            params.add(Integer.parseInt(sensorId));
        }
        if (techId != null && !techId.isBlank()) {
            sql.append(" AND m.tech_id = ?");
            params.add(Integer.parseInt(techId));
        }
        if (eventType != null && !eventType.isBlank()) {
            sql.append(" AND m.event_type = ?");
            params.add(eventType);
        }
        sql.append(" ORDER BY m.event_date DESC");

        List<MaintenanceRecord> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                Object p = params.get(i);
                if (p instanceof String) stmt.setString(i + 1, (String) p);
                else if (p instanceof Integer) stmt.setInt(i + 1, (Integer) p);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public void save(MaintenanceRecord record) throws SQLException {
        String sql = "INSERT INTO MaintenanceEvent (sensor_id, tech_id, event_type, event_date, notes) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, record.getSensorId());
            stmt.setInt(2, record.getTechId());
            stmt.setString(3, record.getEventType());
            stmt.setTimestamp(4, Timestamp.valueOf(record.getEventDate()));
            stmt.setString(5, record.getNotes());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                record.setMaintenanceId(keys.getInt(1));
            }
        }
    }

    public void update(MaintenanceRecord record) throws SQLException {
        String sql = "UPDATE MaintenanceEvent SET sensor_id = ?, tech_id = ?, event_type = ?, event_date = ?, notes = ? WHERE maintenance_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, record.getSensorId());
            stmt.setInt(2, record.getTechId());
            stmt.setString(3, record.getEventType());
            stmt.setTimestamp(4, Timestamp.valueOf(record.getEventDate()));
            stmt.setString(5, record.getNotes());
            stmt.setInt(6, record.getMaintenanceId());
            stmt.executeUpdate();
        }
    }

    public void delete(int maintenanceId) throws SQLException {
        String sql = "DELETE FROM MaintenanceEvent WHERE maintenance_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, maintenanceId);
            stmt.executeUpdate();
        }
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM MaintenanceEvent";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    /**
     * Get maintenance count grouped by event type.
     */
    public List<Object[]> getCountByEventType() throws SQLException {
        String sql = "SELECT event_type, COUNT(maintenance_id) AS cnt FROM MaintenanceEvent GROUP BY event_type";
        List<Object[]> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Object[]{rs.getString("event_type"), rs.getInt("cnt")});
            }
        }
        return list;
    }

    /**
     * Call stored procedure GetMaintenanceSummary.
     */
    public List<Object[]> getMaintenanceSummary() throws SQLException {
        String sql = "{CALL GetMaintenanceSummary()}";
        List<Object[]> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("event_type"),
                    rs.getInt("event_count"),
                    rs.getInt("sensors_affected"),
                    rs.getInt("technicians_involved")
                });
            }
        }
        return list;
    }

    /**
     * Call stored procedure GetTopTechnicians.
     */
    public List<Object[]> getTopTechnicians(int limit) throws SQLException {
        String sql = "{CALL GetTopTechnicians(?)}";
        List<Object[]> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("tech_id"),
                    rs.getString("name"),
                    rs.getString("specialization"),
                    rs.getInt("maintenance_count"),
                    rs.getString("event_types")
                });
            }
        }
        return list;
    }

    private List<MaintenanceRecord> executeQuery(String sql) throws SQLException {
        List<MaintenanceRecord> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    private MaintenanceRecord mapRow(ResultSet rs) throws SQLException {
        MaintenanceRecord m = new MaintenanceRecord();
        m.setMaintenanceId(rs.getInt("maintenance_id"));
        m.setSensorId(rs.getInt("sensor_id"));
        m.setTechId(rs.getInt("tech_id"));
        m.setEventType(rs.getString("event_type"));
        Timestamp eventDate = rs.getTimestamp("event_date");
        if (eventDate != null) m.setEventDate(eventDate.toLocalDateTime());
        m.setNotes(rs.getString("notes"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) m.setCreatedAt(createdAt.toLocalDateTime());
        try { m.setSensorModel(rs.getString("sensor_model")); } catch (SQLException ignored) {}
        try { m.setTechnicianName(rs.getString("technician_name")); } catch (SQLException ignored) {}
        return m;
    }
}
