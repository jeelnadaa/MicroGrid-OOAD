package com.microgrid.servlet;

/**
 * This servlet serves report pages and report generation requests.
 * It follows GRASP Controller by leaving report composition to ReportService.
 * It keeps presentation flow separate from business logic.
 */

import com.microgrid.model.SensorStatusLog;
import com.microgrid.service.MaintenanceService;
import com.microgrid.service.ReadingService;
import com.microgrid.service.ReportService;
import com.microgrid.service.SensorService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Reports page: /reports
 */
@WebServlet("/reports")
public class ReportServlet extends HttpServlet {

    private final MaintenanceService maintenanceService = new MaintenanceService();
    private final ReadingService readingService = new ReadingService();
    private final SensorService sensorService = new SensorService();
    private final ReportService reportService = new ReportService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            // Average readings by sensor type
            req.setAttribute("avgReadings", readingService.getAverageReadingsBySensorType());

            // Top technicians
            req.setAttribute("topTechnicians", maintenanceService.getTopTechnicians(10));

            // Maintenance summary
            req.setAttribute("maintenanceSummary", maintenanceService.getMaintenanceSummary());

            // Sensor status distribution
            req.setAttribute("statusDist", sensorService.getStatusDistribution());

            // Recent status changes
            List<SensorStatusLog> statusLogs = reportService.getRecentStatusLogs(20);
            req.setAttribute("statusLogs", statusLogs);

            req.getRequestDispatcher("/WEB-INF/jsp/reports/index.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Error loading reports", e);
        }
    }
}
