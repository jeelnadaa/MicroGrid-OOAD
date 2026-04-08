package com.microgrid.service;

/**
 * This service manages sensor type behavior and validation.
 * It follows GRASP Controller by delegating persistence to SensorTypeDAO
 * and keeping type-specific business logic separate from the servlet.
 */

import com.microgrid.dao.SensorTypeDAO;
import com.microgrid.model.SensorType;

import java.sql.SQLException;
import java.util.List;

/**
 * Service layer for SensorType operations.
 */
public class SensorTypeService {

    private final SensorTypeDAO dao = new SensorTypeDAO();

    public List<SensorType> getAll() throws SQLException {
        return dao.findAll();
    }

    public List<SensorType> search(String keyword) throws SQLException {
        if (keyword == null || keyword.isBlank()) return dao.findAll();
        return dao.search(keyword);
    }

    public SensorType getById(int typeId) throws SQLException {
        return dao.findById(typeId);
    }

    public void create(String name, String description) throws SQLException {
        SensorType st = new SensorType(name, description);
        dao.save(st);
    }

    public void update(int typeId, String name, String description) throws SQLException {
        SensorType st = dao.findById(typeId);
        if (st != null) {
            st.setName(name);
            st.setDescription(description);
            dao.update(st);
        }
    }

    public void delete(int typeId) throws SQLException {
        dao.delete(typeId);
    }
}
