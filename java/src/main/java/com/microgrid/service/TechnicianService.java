package com.microgrid.service;

/**
 * This service handles technician business operations.
 * It follows SOLID single responsibility by limiting itself to technician
 * rules and delegating persistence to TechnicianDAO.
 */

import com.microgrid.dao.TechnicianDAO;
import com.microgrid.model.Technician;

import java.sql.SQLException;
import java.util.List;

/**
 * Service layer for Technician CRUD operations.
 */
public class TechnicianService {

    private final TechnicianDAO technicianDAO = new TechnicianDAO();

    public List<Technician> getAll() throws SQLException {
        return technicianDAO.findAll();
    }

    public List<Technician> search(String keyword) throws SQLException {
        if (keyword == null || keyword.isBlank()) return getAll();
        return technicianDAO.search(keyword);
    }

    public Technician getById(int techId) throws SQLException {
        return technicianDAO.findById(techId);
    }

    public void create(String name, String contactNo, String specialization) throws SQLException {
        Technician tech = new Technician(name, contactNo, specialization);
        technicianDAO.save(tech);
    }

    public void update(int techId, String name, String contactNo, String specialization) throws SQLException {
        Technician tech = technicianDAO.findById(techId);
        if (tech == null) throw new IllegalArgumentException("Technician not found.");
        tech.setName(name);
        tech.setContactNo(contactNo);
        tech.setSpecialization(specialization);
        technicianDAO.update(tech);
    }

    public void delete(int techId) throws SQLException {
        technicianDAO.delete(techId);
    }

    public int count() throws SQLException {
        return technicianDAO.count();
    }

    public int getMaintenanceCount(int techId) throws SQLException {
        return technicianDAO.getMaintenanceCount(techId);
    }
}
