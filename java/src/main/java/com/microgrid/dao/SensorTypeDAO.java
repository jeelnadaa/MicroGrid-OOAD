package com.microgrid.dao;

/**
 * This DAO is responsible for SensorType storage and retrieval.
 * It follows GRASP Information Expert by owning the SQL queries for this entity.
 * The class keeps database concerns separate from the service and model layers.
 */

import com.microgrid.model.SensorType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for SensorType entity.
 */
public class SensorTypeDAO {

    public SensorType findById(int typeId) throws SQLException {
        String sql = "SELECT * FROM SensorType WHERE type_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, typeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        }
        return null;
    }

    public List<SensorType> findAll() throws SQLException {
        String sql = "SELECT * FROM SensorType ORDER BY name";
        List<SensorType> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<SensorType> search(String keyword) throws SQLException {
        String sql = "SELECT * FROM SensorType WHERE name LIKE ? ORDER BY name";
        List<SensorType> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public void save(SensorType sensorType) throws SQLException {
        String sql = "INSERT INTO SensorType (name, description) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, sensorType.getName());
            stmt.setString(2, sensorType.getDescription());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                sensorType.setTypeId(keys.getInt(1));
            }
        }
    }

    public void update(SensorType sensorType) throws SQLException {
        String sql = "UPDATE SensorType SET name = ?, description = ? WHERE type_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sensorType.getName());
            stmt.setString(2, sensorType.getDescription());
            stmt.setInt(3, sensorType.getTypeId());
            stmt.executeUpdate();
        }
    }

    public void delete(int typeId) throws SQLException {
        String sql = "DELETE FROM SensorType WHERE type_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, typeId);
            stmt.executeUpdate();
        }
    }

    private SensorType mapRow(ResultSet rs) throws SQLException {
        SensorType st = new SensorType();
        st.setTypeId(rs.getInt("type_id"));
        st.setName(rs.getString("name"));
        st.setDescription(rs.getString("description"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) st.setCreatedAt(createdAt.toLocalDateTime());
        return st;
    }
}
