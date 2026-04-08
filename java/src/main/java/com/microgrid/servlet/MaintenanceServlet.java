package com.microgrid.servlet;

/**
 * This servlet routes maintenance-related HTTP requests.
 * It follows GRASP Controller by handling request flow and delegating
 * work to MaintenanceService. It keeps presentation and business logic
 * separate, obeying SOLID practices.
 */

import com.microgrid.model.MaintenanceRecord;
import com.microgrid.service.MaintenanceService;
import com.microgrid.service.SensorService;
import com.microgrid.service.TechnicianService;
import com.microgrid.util.FlashMessage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller for Maintenance CRUD: /maintenance/*
 */
@WebServlet(urlPatterns = {"/maintenance", "/maintenance/create", "/maintenance/edit", "/maintenance/delete"})
public class MaintenanceServlet extends HttpServlet {

    private final MaintenanceService maintenanceService = new MaintenanceService();
    private final SensorService sensorService = new SensorService();
    private final TechnicianService technicianService = new TechnicianService();
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();
        try {
            if ("/maintenance/create".equals(path)) {
                loadFormData(req);
                req.getRequestDispatcher("/WEB-INF/jsp/maintenance/form.jsp").forward(req, resp);
            } else if ("/maintenance/edit".equals(path)) {
                int id = Integer.parseInt(req.getParameter("id"));
                MaintenanceRecord record = maintenanceService.getById(id);
                if (record == null) { resp.sendError(404); return; }
                req.setAttribute("maintenance", record);
                loadFormData(req);
                req.getRequestDispatcher("/WEB-INF/jsp/maintenance/form.jsp").forward(req, resp);
            } else {
                String sensorFilter = req.getParameter("sensor");
                String techFilter = req.getParameter("tech");
                String eventFilter = req.getParameter("event_type");

                List<MaintenanceRecord> events = maintenanceService.getFiltered(sensorFilter, techFilter, eventFilter);
                req.setAttribute("maintenanceEvents", events);
                req.setAttribute("sensorFilter", sensorFilter != null ? sensorFilter : "");
                req.setAttribute("techFilter", techFilter != null ? techFilter : "");
                req.setAttribute("eventFilter", eventFilter != null ? eventFilter : "");
                loadFormData(req);
                req.getRequestDispatcher("/WEB-INF/jsp/maintenance/list.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();
        try {
            if ("/maintenance/create".equals(path)) {
                maintenanceService.create(
                    Integer.parseInt(req.getParameter("sensor_id")),
                    Integer.parseInt(req.getParameter("tech_id")),
                    req.getParameter("event_type"),
                    LocalDateTime.parse(req.getParameter("event_date"), DTF),
                    req.getParameter("notes")
                );
                FlashMessage.flash(req, "Maintenance event created successfully!", "success");
            } else if ("/maintenance/edit".equals(path)) {
                int id = Integer.parseInt(req.getParameter("id"));
                maintenanceService.update(id,
                    Integer.parseInt(req.getParameter("sensor_id")),
                    Integer.parseInt(req.getParameter("tech_id")),
                    req.getParameter("event_type"),
                    LocalDateTime.parse(req.getParameter("event_date"), DTF),
                    req.getParameter("notes")
                );
                FlashMessage.flash(req, "Maintenance event updated successfully!", "success");
            } else if ("/maintenance/delete".equals(path)) {
                int id = Integer.parseInt(req.getParameter("id"));
                maintenanceService.delete(id);
                FlashMessage.flash(req, "Maintenance event deleted successfully!", "success");
            }
        } catch (Exception e) {
            FlashMessage.flash(req, "Error: " + e.getMessage(), "danger");
        }
        resp.sendRedirect(req.getContextPath() + "/maintenance");
    }

    private void loadFormData(HttpServletRequest req) throws Exception {
        req.setAttribute("sensors", sensorService.getAll());
        req.setAttribute("technicians", technicianService.getAll());
    }
}
