package com.microgrid.service;

/**
 * This service encapsulates reading-related business behavior.
 * It follows SOLID single responsibility by focusing on read operations,
 * and GRASP Controller by coordinating between DAO objects and the servlet.
 * It creates domain objects only when needed and keeps higher-level logic
 * out of the persistence layer.
 */

import com.microgrid.dao.ReadingDAO;
import com.microgrid.model.Reading;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer for Reading CRUD operations.
 */
public class ReadingService {

    private final ReadingDAO readingDAO = new ReadingDAO();

    public List<Reading> getAll(int limit, int offset) throws SQLException {
        return readingDAO.findAll(limit, offset);
    }

    public List<Reading> getBySensorId(int sensorId) throws SQLException {
        return readingDAO.findBySensorId(sensorId);
    }

    public List<Reading> getSensorReadings(int sensorId) throws SQLException {
        return readingDAO.getSensorReadings(sensorId);
    }

    public Reading getById(long readingId) throws SQLException {
        return readingDAO.findById(readingId);
    }

    public void create(int sensorId, BigDecimal value, LocalDateTime timestamp) throws SQLException {
        Reading reading = new Reading(sensorId, value, timestamp);
        readingDAO.save(reading);
    }

    public void update(long readingId, int sensorId, BigDecimal value, LocalDateTime timestamp) throws SQLException {
        Reading reading = readingDAO.findById(readingId);
        if (reading == null) throw new IllegalArgumentException("Reading not found.");
        reading.setSensorId(sensorId);
        reading.setReadingValue(value);
        reading.setReadingTimestamp(timestamp);
        readingDAO.update(reading);
    }

    public void delete(long readingId) throws SQLException {
        readingDAO.delete(readingId);
    }

    public int count() throws SQLException {
        return readingDAO.count();
    }

    public List<Reading> getRecent(int limit) throws SQLException {
        return readingDAO.findRecent(limit);
    }

    public List<Object[]> getAverageReadingsBySensorType() throws SQLException {
        return readingDAO.getAverageReadingsBySensorType();
    }
}
