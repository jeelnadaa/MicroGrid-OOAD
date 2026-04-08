package com.microgrid.service;

/**
 * This service contains business rules for location management.
 * It follows GRASP Controller by coordinating between the servlet and DAO.
 * It also uses Creator responsibilities when it constructs Location objects
 * and then delegates persistence to LocationDAO.
 */

import com.microgrid.dao.LocationDAO;
import com.microgrid.model.Location;

import java.sql.SQLException;
import java.util.List;

/**
 * Service layer for Location operations.
 */
public class LocationService {

    private final LocationDAO dao = new LocationDAO();

    public List<Location> getAll() throws SQLException {
        return dao.findAll();
    }

    public List<Location> search(String keyword) throws SQLException {
        if (keyword == null || keyword.isBlank()) return dao.findAll();
        return dao.search(keyword);
    }

    public Location getById(int locationId) throws SQLException {
        return dao.findById(locationId);
    }

    public void create(String areaName, double latitude, double longitude, double elevation) throws SQLException {
        Location loc = new Location(areaName, latitude, longitude);
        loc.setElevation(elevation);
        dao.save(loc);
    }

    public void update(int locationId, String areaName, double latitude, double longitude, double elevation) throws SQLException {
        Location loc = dao.findById(locationId);
        if (loc != null) {
            loc.setAreaName(areaName);
            loc.setLatitude(latitude);
            loc.setLongitude(longitude);
            loc.setElevation(elevation);
            dao.update(loc);
        }
    }

    public void delete(int locationId) throws SQLException {
        dao.delete(locationId);
    }

    public int count() throws SQLException {
        return dao.count();
    }
}
