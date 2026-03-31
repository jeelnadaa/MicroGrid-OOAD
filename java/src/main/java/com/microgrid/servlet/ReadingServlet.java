package com.microgrid.servlet;

/**
 * This servlet handles reading entry and search requests.
 * It follows GRASP Controller by mapping request paths to service operations.
 * It keeps business rules separately in ReadingService.
 */

import com.microgrid.model.Reading;
import com.microgrid.model.Sensor;
import com.microgrid.service.ReadingService;
import com.microgrid.service.SensorService;
import com.microgrid.util.FlashMessage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller for Reading CRUD: /readings/*
 */
@WebServlet(urlPatterns = {"/readings", "/readings/create", "/readings/edit", "/readings/delete"})
public class ReadingServlet extends HttpServlet {

    private final ReadingService readingService = new ReadingService();
    private final SensorService sensorService = new SensorService();
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();
        try {
            if ("/readings/create".equals(path)) {
                req.setAttribute("sensors", sensorService.getAll());
                req.getRequestDispatcher("/WEB-INF/jsp/readings/form.jsp").forward(req, resp);
            } else if ("/readings/edit".equals(path)) {
                long id = Long.parseLong(req.getParameter("id"));
                Reading reading = readingService.getById(id);
                if (reading == null) { resp.sendError(404); return; }
                req.setAttribute("reading", reading);
                req.setAttribute("sensors", sensorService.getAll());
                req.getRequestDispatcher("/WEB-INF/jsp/readings/form.jsp").forward(req, resp);
            } else {
                String sensorFilter = req.getParameter("sensor");
                int page = 1;
                try { page = Integer.parseInt(req.getParameter("page")); } catch (Exception ignored) {}
                int perPage = 50;
                int offset = (page - 1) * perPage;

                List<Reading> readings;
                if (sensorFilter != null && !sensorFilter.isBlank()) {
                    readings = readingService.getBySensorId(Integer.parseInt(sensorFilter));
                } else {
                    readings = readingService.getAll(perPage, offset);
                }

                req.setAttribute("readings", readings);
                req.setAttribute("sensors", sensorService.getAll());
                req.setAttribute("sensorFilter", sensorFilter != null ? sensorFilter : "");
                req.setAttribute("currentPage", page);
                req.getRequestDispatcher("/WEB-INF/jsp/readings/list.jsp").forward(req, resp);
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
            if ("/readings/create".equals(path)) {
                readingService.create(
                    Integer.parseInt(req.getParameter("sensor_id")),
                    new BigDecimal(req.getParameter("reading_value")),
                    LocalDateTime.parse(req.getParameter("reading_timestamp"), DTF)
                );
                FlashMessage.flash(req, "Reading recorded successfully!", "success");
            } else if ("/readings/edit".equals(path)) {
                long id = Long.parseLong(req.getParameter("id"));
                readingService.update(id,
                    Integer.parseInt(req.getParameter("sensor_id")),
                    new BigDecimal(req.getParameter("reading_value")),
                    LocalDateTime.parse(req.getParameter("reading_timestamp"), DTF)
                );
                FlashMessage.flash(req, "Reading updated successfully!", "success");
            } else if ("/readings/delete".equals(path)) {
                long id = Long.parseLong(req.getParameter("id"));
                readingService.delete(id);
                FlashMessage.flash(req, "Reading deleted successfully!", "success");
            }
        } catch (Exception e) {
            FlashMessage.flash(req, "Error: " + e.getMessage(), "danger");
        }
        resp.sendRedirect(req.getContextPath() + "/readings");
    }
}
