package com.microgrid.servlet;

/**
 * This servlet generates export responses for the client.
 * It follows GRASP Controller by handling HTTP flow and delegating
 * report generation to service classes.
 * It keeps its own focus on request/response handling only.
 */

import com.microgrid.dao.*;
import com.microgrid.model.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * CSV export controller: /export/*
 */
@WebServlet(urlPatterns = {"/export/sensors/csv", "/export/readings/csv", "/export/locations/csv",
        "/export/technicians/csv", "/export/maintenance/csv", "/export/sensor-types/csv"})
public class ExportServlet extends HttpServlet {

    private final SensorDAO sensorDAO = new SensorDAO();
    private final ReadingDAO readingDAO = new ReadingDAO();
    private final LocationDAO locationDAO = new LocationDAO();
    private final TechnicianDAO technicianDAO = new TechnicianDAO();
    private final MaintenanceDAO maintenanceDAO = new MaintenanceDAO();
    private final SensorTypeDAO sensorTypeDAO = new SensorTypeDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();
        resp.setContentType("text/csv");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter writer = resp.getWriter()) {
            switch (path) {
                case "/export/sensors/csv" -> {
                    resp.setHeader("Content-Disposition", "attachment; filename=sensors_export.csv");
                    exportSensors(writer);
                }
                case "/export/readings/csv" -> {
                    resp.setHeader("Content-Disposition", "attachment; filename=readings_export.csv");
                    exportReadings(writer);
                }
                case "/export/locations/csv" -> {
                    resp.setHeader("Content-Disposition", "attachment; filename=locations_export.csv");
                    exportLocations(writer);
                }
                case "/export/technicians/csv" -> {
                    resp.setHeader("Content-Disposition", "attachment; filename=technicians_export.csv");
                    exportTechnicians(writer);
                }
                case "/export/maintenance/csv" -> {
                    resp.setHeader("Content-Disposition", "attachment; filename=maintenance_export.csv");
                    exportMaintenance(writer);
                }
                case "/export/sensor-types/csv" -> {
                    resp.setHeader("Content-Disposition", "attachment; filename=sensor_types_export.csv");
                    exportSensorTypes(writer);
                }
                default -> resp.sendError(404);
            }
        } catch (Exception e) {
            throw new ServletException("Export error", e);
        }
    }

    private void exportSensors(PrintWriter w) throws Exception {
        w.println("ID,Model,Type,Location,Install Date,Status,Created At");
        for (Sensor s : sensorDAO.findAll()) {
            w.printf("%d,%s,%s,%s,%s,%s,%s%n",
                s.getSensorId(), csvEscape(s.getModel()),
                csvEscape(s.getSensorTypeName()), csvEscape(s.getLocationName()),
                s.getInstallDate(), s.getStatus(),
                s.getCreatedAt() != null ? s.getCreatedAt() : "");
        }
    }

    private void exportReadings(PrintWriter w) throws Exception {
        w.println("Reading ID,Sensor ID,Sensor Model,Sensor Type,Location,Reading Value,Timestamp");
        for (Reading r : readingDAO.findAll(10000, 0)) {
            w.printf("%d,%d,%s,%s,%s,%s,%s%n",
                r.getReadingId(), r.getSensorId(),
                csvEscape(r.getSensorModel()), csvEscape(r.getSensorTypeName()),
                csvEscape(r.getLocationName()), r.getReadingValue(),
                r.getReadingTimestamp() != null ? r.getReadingTimestamp() : "");
        }
    }

    private void exportLocations(PrintWriter w) throws Exception {
        w.println("ID,Area Name,Latitude,Longitude,Elevation,Created At");
        for (Location l : locationDAO.findAll()) {
            w.printf("%d,%s,%.6f,%.6f,%.2f,%s%n",
                l.getLocationId(), csvEscape(l.getAreaName()),
                l.getLatitude(), l.getLongitude(), l.getElevation(),
                l.getCreatedAt() != null ? l.getCreatedAt() : "");
        }
    }

    private void exportTechnicians(PrintWriter w) throws Exception {
        w.println("ID,Name,Contact Number,Specialization,Created At");
        for (Technician t : technicianDAO.findAll()) {
            w.printf("%d,%s,%s,%s,%s%n",
                t.getTechId(), csvEscape(t.getName()),
                csvEscape(t.getContactNo()), csvEscape(t.getSpecialization()),
                t.getCreatedAt() != null ? t.getCreatedAt() : "");
        }
    }

    private void exportMaintenance(PrintWriter w) throws Exception {
        w.println("ID,Sensor Model,Technician,Event Type,Event Date,Notes,Created At");
        for (MaintenanceRecord m : maintenanceDAO.findAll()) {
            w.printf("%d,%s,%s,%s,%s,%s,%s%n",
                m.getMaintenanceId(), csvEscape(m.getSensorModel()),
                csvEscape(m.getTechnicianName()), m.getEventType(),
                m.getEventDate() != null ? m.getEventDate() : "",
                csvEscape(m.getNotes()),
                m.getCreatedAt() != null ? m.getCreatedAt() : "");
        }
    }

    private void exportSensorTypes(PrintWriter w) throws Exception {
        w.println("ID,Name,Description,Created At");
        for (SensorType st : sensorTypeDAO.findAll()) {
            w.printf("%d,%s,%s,%s%n",
                st.getTypeId(), csvEscape(st.getName()),
                csvEscape(st.getDescription()),
                st.getCreatedAt() != null ? st.getCreatedAt() : "");
        }
    }

    private String csvEscape(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
