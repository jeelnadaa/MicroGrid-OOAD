package com.microgrid.servlet;

/**
 * This servlet manages sensor CRUD web requests.
 * It follows GRASP Controller by forwarding requests to SensorService.
 * It keeps its own logic limited to HTTP handling and response creation.
 */

import com.microgrid.model.Location;
import com.microgrid.model.Reading;
import com.microgrid.model.Sensor;
import com.microgrid.model.SensorType;
import com.microgrid.service.LocationService;
import com.microgrid.service.ReadingService;
import com.microgrid.service.SensorService;
import com.microgrid.service.SensorTypeService;
import com.microgrid.util.FlashMessage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller for Sensor CRUD: /sensors/*
 */
@WebServlet(urlPatterns = {"/sensors", "/sensors/create", "/sensors/edit", "/sensors/delete", "/sensors/readings"})
public class SensorServlet extends HttpServlet {

    private final SensorService sensorService = new SensorService();
    private final SensorTypeService sensorTypeService = new SensorTypeService();
    private final LocationService locationService = new LocationService();
    private final ReadingService readingService = new ReadingService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();
        try {
            if ("/sensors/create".equals(path)) {
                loadFormData(req);
                req.getRequestDispatcher("/WEB-INF/jsp/sensors/form.jsp").forward(req, resp);
            } else if ("/sensors/edit".equals(path)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Sensor sensor = sensorService.getById(id);
                if (sensor == null) { resp.sendError(404); return; }
                req.setAttribute("sensor", sensor);
                loadFormData(req);
                req.getRequestDispatcher("/WEB-INF/jsp/sensors/form.jsp").forward(req, resp);
            } else if ("/sensors/readings".equals(path)) {
                int sensorId = Integer.parseInt(req.getParameter("id"));
                Sensor sensor = sensorService.getById(sensorId);
                if (sensor == null) { resp.sendError(404); return; }
                List<Reading> readings = readingService.getSensorReadings(sensorId);
                req.setAttribute("sensor", sensor);
                req.setAttribute("readings", readings);
                req.getRequestDispatcher("/WEB-INF/jsp/sensors/readings.jsp").forward(req, resp);
            } else {
                // List with filters
                String search = req.getParameter("search");
                String status = req.getParameter("status");
                String type = req.getParameter("type");
                String location = req.getParameter("location");

                List<Sensor> sensors = sensorService.getFiltered(search, status, type, location);
                req.setAttribute("sensors", sensors);
                req.setAttribute("search", search != null ? search : "");
                req.setAttribute("statusFilter", status != null ? status : "");
                req.setAttribute("typeFilter", type != null ? type : "");
                req.setAttribute("locationFilter", location != null ? location : "");
                loadFormData(req);
                req.getRequestDispatcher("/WEB-INF/jsp/sensors/list.jsp").forward(req, resp);
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
            if ("/sensors/create".equals(path)) {
                sensorService.create(
                    req.getParameter("model"),
                    LocalDate.parse(req.getParameter("install_date")),
                    req.getParameter("status"),
                    Integer.parseInt(req.getParameter("type_id")),
                    Integer.parseInt(req.getParameter("location_id"))
                );
                FlashMessage.flash(req, "Sensor created successfully!", "success");
            } else if ("/sensors/edit".equals(path)) {
                int id = Integer.parseInt(req.getParameter("id"));
                sensorService.update(id,
                    req.getParameter("model"),
                    LocalDate.parse(req.getParameter("install_date")),
                    req.getParameter("status"),
                    Integer.parseInt(req.getParameter("type_id")),
                    Integer.parseInt(req.getParameter("location_id"))
                );
                FlashMessage.flash(req, "Sensor updated successfully!", "success");
            } else if ("/sensors/delete".equals(path)) {
                int id = Integer.parseInt(req.getParameter("id"));
                sensorService.delete(id);
                FlashMessage.flash(req, "Sensor deleted successfully!", "success");
            }
        } catch (Exception e) {
            FlashMessage.flash(req, "Error: " + e.getMessage(), "danger");
        }
        resp.sendRedirect(req.getContextPath() + "/sensors");
    }

    private void loadFormData(HttpServletRequest req) throws Exception {
        req.setAttribute("sensorTypes", sensorTypeService.getAll());
        req.setAttribute("locations", locationService.getAll());
    }
}
