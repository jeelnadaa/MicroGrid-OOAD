package com.microgrid.servlet;

/**
 * This servlet routes sensor type management actions.
 * It follows GRASP Controller by delegating business logic to SensorTypeService.
 * It keeps persistence concerns out of the servlet.
 */

import com.microgrid.service.SensorTypeService;
import com.microgrid.model.SensorType;
import com.microgrid.util.FlashMessage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Controller for SensorType CRUD: /sensor-types/*
 */
@WebServlet(urlPatterns = {"/sensor-types", "/sensor-types/create", "/sensor-types/edit", "/sensor-types/delete"})
public class SensorTypeServlet extends HttpServlet {

    private final SensorTypeService service = new SensorTypeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();
        try {
            if ("/sensor-types/create".equals(path)) {
                req.getRequestDispatcher("/WEB-INF/jsp/sensor_types/form.jsp").forward(req, resp);
            } else if ("/sensor-types/edit".equals(path)) {
                int typeId = Integer.parseInt(req.getParameter("id"));
                SensorType st = service.getById(typeId);
                if (st == null) { resp.sendError(404); return; }
                req.setAttribute("sensorType", st);
                req.getRequestDispatcher("/WEB-INF/jsp/sensor_types/form.jsp").forward(req, resp);
            } else {
                String search = req.getParameter("search");
                List<SensorType> sensorTypes = service.search(search);
                req.setAttribute("sensorTypes", sensorTypes);
                req.setAttribute("search", search != null ? search : "");
                req.getRequestDispatcher("/WEB-INF/jsp/sensor_types/list.jsp").forward(req, resp);
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
            if ("/sensor-types/create".equals(path)) {
                service.create(req.getParameter("name"), req.getParameter("description"));
                FlashMessage.flash(req, "Sensor type created successfully!", "success");
            } else if ("/sensor-types/edit".equals(path)) {
                int typeId = Integer.parseInt(req.getParameter("id"));
                service.update(typeId, req.getParameter("name"), req.getParameter("description"));
                FlashMessage.flash(req, "Sensor type updated successfully!", "success");
            } else if ("/sensor-types/delete".equals(path)) {
                int typeId = Integer.parseInt(req.getParameter("id"));
                service.delete(typeId);
                FlashMessage.flash(req, "Sensor type deleted successfully!", "success");
            }
        } catch (Exception e) {
            FlashMessage.flash(req, "Error: " + e.getMessage(), "danger");
        }
        resp.sendRedirect(req.getContextPath() + "/sensor-types");
    }
}
