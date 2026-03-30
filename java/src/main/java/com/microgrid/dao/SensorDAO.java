package com.microgrid.dao;

/**
 * This DAO encapsulates all database operations for Sensor entities.
 * It follows GRASP Information Expert by containing the knowledge needed to
 * retrieve and update sensor records. It keeps persistence separate from
 * business logic, which is an important SOLID separation of concerns.
 */

import com.microgrid.model.Sensor;
import com.microgrid.model.enums.SensorStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Sensor entity.
 */
public class SensorDAO {

    public Sensor findById(int sensorId) throws SQLException {
        String sql = """
            SELECT s.*, st.name AS sensor_type_name, l.area_name AS location_name
            FROM Sensor s
            JOIN SensorType st ON s.type_id = st.type_id
            JOIN Location l ON s.location_id = l.location_id
            WHERE s.sensor_id = ?
            """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sensorId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        }
        return null;
    }

    public List<Sensor> findAll() throws SQLException {
        String sql = """
            SELECT s.*, st.name AS sensor_type_name, l.area_name AS location_name
            FROM Sensor s
            JOIN SensorType st ON s.type_id = st.type_id
            JOIN Location l ON s.location_id = l.location_id
            ORDER BY s.sensor_id DESC
            """;
        return executeQuery(sql);
    }

    public List<Sensor> findFiltered(String search, String status, String typeId, String locationId) throws SQLException {
        StringBuilder sql = new StringBuilder("""
            SELECT s.*, st.name AS sensor_type_name, l.area_name AS location_name
            FROM Sensor s
            JOIN SensorType st ON s.type_id = st.type_id
            JOIN Location l ON s.location_id = l.location_id
            WHERE 1=1
            """);
        List<Object> params = new ArrayList<>();

        if (search != null && !search.isBlank()) {
            sql.append(" AND s.model LIKE ?");
            params.add("%" + search + "%");
        }
        if (status != null && !status.isBlank()) {
            sql.append(" AND s.status = ?");
            params.add(status);
        }
        if (typeId != null && !typeId.isBlank()) {
            sql.append(" AND s.type_id = ?");
            params.add(Integer.parseInt(typeId));
        }
        if (locationId != null && !locationId.isBlank()) {
            sql.append(" AND s.location_id = ?");
            params.add(Integer.parseInt(locationId));
        }
        sql.append(" ORDER BY s.sensor_id DESC");

        List<Sensor> list = new ArrayList<>();
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

    public void save(Sensor sensor) throws SQLException {
        String sql = "INSERT INTO Sensor (model, install_date, status, type_id, location_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, sensor.getModel());
            stmt.setDate(2, Date.valueOf(sensor.getInstallDate()));
            stmt.setString(3, sensor.getStatus().getValue());
            stmt.setInt(4, sensor.getTypeId());
            stmt.setInt(5, sensor.getLocationId());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                sensor.setSensorId(keys.getInt(1));
            }
        }
    }

    public void update(Sensor sensor) throws SQLException {
        String sql = "UPDATE Sensor SET model = ?, install_date = ?, status = ?, type_id = ?, location_id = ? WHERE sensor_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sensor.getModel());
            stmt.setDate(2, Date.valueOf(sensor.getInstallDate()));
            stmt.setString(3, sensor.getStatus().getValue());
            stmt.setInt(4, sensor.getTypeId());
            stmt.setInt(5, sensor.getLocationId());
            stmt.setInt(6, sensor.getSensorId());
            stmt.executeUpdate();
        }
    }

    public void delete(int sensorId) throws SQLException {
        String sql = "DELETE FROM Sensor WHERE sensor_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sensorId);
            stmt.executeUpdate();
        }
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Sensor";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public int countByStatus(SensorStatus status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Sensor WHERE status = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.getValue());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    /**
     * Get sensor status distribution: returns list of [status, count].
     */
    public List<Object[]> getStatusDistribution() throws SQLException {
        String sql = "SELECT status, COUNT(sensor_id) AS cnt FROM Sensor GROUP BY status";
        List<Object[]> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Object[]{rs.getString("status"), rs.getInt("cnt")});
            }
        }
        return list;
    }

    private List<Sensor> executeQuery(String sql) throws SQLException {
        List<Sensor> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    private Sensor mapRow(ResultSet rs) throws SQLException {
        Sensor s = new Sensor();
        s.setSensorId(rs.getInt("sensor_id"));
        s.setModel(rs.getString("model"));
        Date installDate = rs.getDate("install_date");
        if (installDate != null) s.setInstallDate(installDate.toLocalDate());
        s.setStatus(SensorStatus.fromString(rs.getString("status")));
        s.setTypeId(rs.getInt("type_id"));
        s.setLocationId(rs.getInt("location_id"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) s.setCreatedAt(createdAt.toLocalDateTime());
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) s.setUpdatedAt(updatedAt.toLocalDateTime());
        // Transient fields from JOIN
        try {
            s.setSensorTypeName(rs.getString("sensor_type_name"));
            s.setLocationName(rs.getString("location_name"));
        } catch (SQLException ignored) {
            // These columns may not exist in all queries
        }
        return s;
    }
}
