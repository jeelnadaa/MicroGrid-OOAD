package com.microgrid.service;

/**
 * This service handles maintenance business rules and validation.
 * It follows GRASP Controller by coordinating DAOs and domain objects.
 * It also follows SOLID single responsibility by keeping maintenance logic
 * separate from persistence and web presentation.
 */

import com.microgrid.dao.MaintenanceDAO;
import com.microgrid.model.MaintenanceRecord;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer for MaintenanceRecord CRUD operations.
 */
public class MaintenanceService {

    private final MaintenanceDAO maintenanceDAO = new MaintenanceDAO();

    public List<MaintenanceRecord> getAll() throws SQLException {
        return maintenanceDAO.findAll();
    }

    public List<MaintenanceRecord> getFiltered(String sensorId, String techId, String eventType) throws SQLException {
        return maintenanceDAO.findFiltered(sensorId, techId, eventType);
    }

    public MaintenanceRecord getById(int maintenanceId) throws SQLException {
        return maintenanceDAO.findById(maintenanceId);
    }

    public void create(int sensorId, int techId, String eventType, LocalDateTime eventDate, String notes) throws SQLException {
        MaintenanceRecord record = new MaintenanceRecord(sensorId, techId, eventType, eventDate, notes);
        maintenanceDAO.save(record);
    }

    public void update(int maintenanceId, int sensorId, int techId, String eventType, LocalDateTime eventDate, String notes) throws SQLException {
        MaintenanceRecord record = maintenanceDAO.findById(maintenanceId);
        if (record == null) throw new IllegalArgumentException("Maintenance record not found.");
        record.setSensorId(sensorId);
        record.setTechId(techId);
        record.setEventType(eventType);
        record.setEventDate(eventDate);
        record.setNotes(notes);
        maintenanceDAO.update(record);
    }

    public void delete(int maintenanceId) throws SQLException {
        maintenanceDAO.delete(maintenanceId);
    }

    public int count() throws SQLException {
        return maintenanceDAO.count();
    }

    public List<Object[]> getCountByEventType() throws SQLException {
        return maintenanceDAO.getCountByEventType();
    }

    public List<Object[]> getMaintenanceSummary() throws SQLException {
        return maintenanceDAO.getMaintenanceSummary();
    }

    public List<Object[]> getTopTechnicians(int limit) throws SQLException {
        return maintenanceDAO.getTopTechnicians(limit);
    }
}
