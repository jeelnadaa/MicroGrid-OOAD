package com.microgrid.service;

/**
 * This service contains sensor-specific domain operations.
 * It follows GRASP Controller by coordinating persistence and validation,
 * and SOLID Single Responsibility by keeping sensor logic separate from
 * DAO and servlet layers.
 */

import com.microgrid.dao.SensorDAO;
import com.microgrid.model.Sensor;
import com.microgrid.model.enums.SensorStatus;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Service layer for Sensor CRUD and status management.
 */
public class SensorService {

    private final SensorDAO sensorDAO = new SensorDAO();

    public List<Sensor> getAll() throws SQLException {
        return sensorDAO.findAll();
    }

    public List<Sensor> getFiltered(String search, String status, String typeId, String locationId) throws SQLException {
        return sensorDAO.findFiltered(search, status, typeId, locationId);
    }

    public Sensor getById(int sensorId) throws SQLException {
        return sensorDAO.findById(sensorId);
    }

    public void create(String model, LocalDate installDate, String status, int typeId, int locationId) throws SQLException {
        Sensor sensor = new Sensor(model, installDate, typeId, locationId);
        sensor.setStatus(SensorStatus.fromString(status));
        sensorDAO.save(sensor);
    }

    public void update(int sensorId, String model, LocalDate installDate, String status, int typeId, int locationId) throws SQLException {
        Sensor sensor = sensorDAO.findById(sensorId);
        if (sensor == null) throw new IllegalArgumentException("Sensor not found.");
        sensor.setModel(model);
        sensor.setInstallDate(installDate);
        sensor.updateStatus(SensorStatus.fromString(status));
        sensor.setTypeId(typeId);
        sensor.setLocationId(locationId);
        sensorDAO.update(sensor);
    }

    public void delete(int sensorId) throws SQLException {
        sensorDAO.delete(sensorId);
    }

    public int count() throws SQLException {
        return sensorDAO.count();
    }

    public int countActive() throws SQLException {
        return sensorDAO.countByStatus(SensorStatus.ACTIVE);
    }

    public List<Object[]> getStatusDistribution() throws SQLException {
        return sensorDAO.getStatusDistribution();
    }
}
