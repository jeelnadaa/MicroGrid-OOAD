package com.microgrid.dao;

/**
 * This DAO class isolates Technician persistence operations.
 * It follows GRASP Information Expert because it knows how to build and
 * retrieve Technician entities from the database. It also respects
 * SOLID single responsibility by not mixing domain processing with SQL.
 */

import com.microgrid.model.Technician;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Technician entity.
 */
public class TechnicianDAO {

    public Technician findById(int techId) throws SQLException {
        String sql = "SELECT * FROM Technician WHERE tech_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, techId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        }
        return null;
    }

    public List<Technician> findAll() throws SQLException {
        String sql = "SELECT * FROM Technician ORDER BY name";
        List<Technician> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Technician> search(String keyword) throws SQLException {
        String sql = "SELECT * FROM Technician WHERE name LIKE ? ORDER BY name";
        List<Technician> list = new ArrayList<>();
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

    public void save(Technician tech) throws SQLException {
        String sql = "INSERT INTO Technician (name, contact_no, specialization) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, tech.getName());
            stmt.setString(2, tech.getContactNo());
            stmt.setString(3, tech.getSpecialization());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                tech.setTechId(keys.getInt(1));
            }
        }
    }

    public void update(Technician tech) throws SQLException {
        String sql = "UPDATE Technician SET name = ?, contact_no = ?, specialization = ? WHERE tech_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tech.getName());
            stmt.setString(2, tech.getContactNo());
            stmt.setString(3, tech.getSpecialization());
            stmt.setInt(4, tech.getTechId());
            stmt.executeUpdate();
        }
    }

    public void delete(int techId) throws SQLException {
        String sql = "DELETE FROM Technician WHERE tech_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, techId);
            stmt.executeUpdate();
        }
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Technician";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    /**
     * Get maintenance count per technician.
     */
    public int getMaintenanceCount(int techId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM MaintenanceEvent WHERE tech_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, techId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    private Technician mapRow(ResultSet rs) throws SQLException {
        Technician t = new Technician();
        t.setTechId(rs.getInt("tech_id"));
        t.setName(rs.getString("name"));
        t.setContactNo(rs.getString("contact_no"));
        t.setSpecialization(rs.getString("specialization"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) t.setCreatedAt(createdAt.toLocalDateTime());
        return t;
    }
}
