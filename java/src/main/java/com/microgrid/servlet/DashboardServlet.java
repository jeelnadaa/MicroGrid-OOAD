package com.microgrid.servlet;

/**
 * This servlet coordinates dashboard data display.
 * It follows GRASP Controller by preparing view state and delegating
 * data gathering to the service layer.
 * It observes SOLID separation by not embedding persistence logic.
 */

import com.microgrid.model.Reading;
import com.microgrid.service.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Dashboard controller — serves the main index page with statistics.
 */
@WebServlet(urlPatterns = {"", "/"})
public class DashboardServlet extends HttpServlet {

    private final SensorService sensorService = new SensorService();
    private final ReadingService readingService = new ReadingService();
    private final LocationService locationService = new LocationService();
    private final TechnicianService technicianService = new TechnicianService();
    private final MaintenanceService maintenanceService = new MaintenanceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("totalSensors", sensorService.count());
            req.setAttribute("activeSensors", sensorService.countActive());
            req.setAttribute("totalReadings", readingService.count());
            req.setAttribute("totalLocations", locationService.count());
            req.setAttribute("totalTechnicians", technicianService.count());
            req.setAttribute("totalMaintenance", maintenanceService.count());

            // Recent readings
            List<Reading> recentReadings = readingService.getRecent(10);
            req.setAttribute("recentReadings", recentReadings);

            // Maintenance stats by type
            req.setAttribute("maintenanceStats", maintenanceService.getCountByEventType());

            // Average readings by sensor type
            req.setAttribute("avgReadings", readingService.getAverageReadingsBySensorType());

            req.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Error loading dashboard", e);
        }
    }
}
