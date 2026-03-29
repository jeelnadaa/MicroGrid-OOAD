package com.microgrid.dao;

/**
 * This DAO keeps location persistence details in one place.
 * It follows GRASP Information Expert by knowing how to construct Location
 * objects from database rows. It also adheres to SOLID single responsibility
 * by focusing exclusively on database access.
 */

import com.microgrid.model.Location;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Location entity.
 */
public class LocationDAO {

    public Location findById(int locationId) throws SQLException {
        String sql = "SELECT * FROM Location WHERE location_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, locationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        }
        return null;
    }

    public List<Location> findAll() throws SQLException {
        String sql = "SELECT * FROM Location ORDER BY area_name";
        List<Location> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Location> search(String keyword) throws SQLException {
        String sql = "SELECT * FROM Location WHERE area_name LIKE ? ORDER BY area_name";
        List<Location> list = new ArrayList<>();
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

    public void save(Location location) throws SQLException {
        String sql = "INSERT INTO Location (area_name, latitude, longitude, elevation) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, location.getAreaName());
            stmt.setDouble(2, location.getLatitude());
            stmt.setDouble(3, location.getLongitude());
            stmt.setDouble(4, location.getElevation());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                location.setLocationId(keys.getInt(1));
            }
        }
    }

    public void update(Location location) throws SQLException {
        String sql = "UPDATE Location SET area_name = ?, latitude = ?, longitude = ?, elevation = ? WHERE location_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, location.getAreaName());
            stmt.setDouble(2, location.getLatitude());
            stmt.setDouble(3, location.getLongitude());
            stmt.setDouble(4, location.getElevation());
            stmt.setInt(5, location.getLocationId());
            stmt.executeUpdate();
        }
    }

    public void delete(int locationId) throws SQLException {
        String sql = "DELETE FROM Location WHERE location_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, locationId);
            stmt.executeUpdate();
        }
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Location";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    private Location mapRow(ResultSet rs) throws SQLException {
        Location loc = new Location();
        loc.setLocationId(rs.getInt("location_id"));
        loc.setAreaName(rs.getString("area_name"));
        loc.setLatitude(rs.getDouble("latitude"));
        loc.setLongitude(rs.getDouble("longitude"));
        loc.setElevation(rs.getDouble("elevation"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) loc.setCreatedAt(createdAt.toLocalDateTime());
        return loc;
    }
}
