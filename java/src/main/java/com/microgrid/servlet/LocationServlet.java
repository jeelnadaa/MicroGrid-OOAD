package com.microgrid.servlet;

/**
 * This servlet controls location CRUD interactions.
 * It follows GRASP Controller by routing actions to the LocationService.
 * It keeps its own responsibilities small by delegating business decisions
 * and persistence to other classes.
 */

import com.microgrid.model.Location;
import com.microgrid.service.LocationService;
import com.microgrid.util.FlashMessage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Location CRUD: /locations/*
 */
@WebServlet(urlPatterns = {"/locations", "/locations/create", "/locations/edit", "/locations/delete"})
public class LocationServlet extends HttpServlet {

    private final LocationService service = new LocationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();
        try {
            if ("/locations/create".equals(path)) {
                req.getRequestDispatcher("/WEB-INF/jsp/locations/form.jsp").forward(req, resp);
            } else if ("/locations/edit".equals(path)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Location loc = service.getById(id);
                if (loc == null) { resp.sendError(404); return; }
                req.setAttribute("location", loc);
                req.getRequestDispatcher("/WEB-INF/jsp/locations/form.jsp").forward(req, resp);
            } else {
                String search = req.getParameter("search");
                List<Location> locations = service.search(search);
                req.setAttribute("locations", locations);
                req.setAttribute("search", search != null ? search : "");
                req.getRequestDispatcher("/WEB-INF/jsp/locations/list.jsp").forward(req, resp);
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
            if ("/locations/create".equals(path)) {
                service.create(
                    req.getParameter("area_name"),
                    Double.parseDouble(req.getParameter("latitude")),
                    Double.parseDouble(req.getParameter("longitude")),
                    Double.parseDouble(req.getParameter("elevation") != null && !req.getParameter("elevation").isBlank() ? req.getParameter("elevation") : "0.0")
                );
                FlashMessage.flash(req, "Location created successfully!", "success");
            } else if ("/locations/edit".equals(path)) {
                int id = Integer.parseInt(req.getParameter("id"));
                service.update(id,
                    req.getParameter("area_name"),
                    Double.parseDouble(req.getParameter("latitude")),
                    Double.parseDouble(req.getParameter("longitude")),
                    Double.parseDouble(req.getParameter("elevation") != null && !req.getParameter("elevation").isBlank() ? req.getParameter("elevation") : "0.0")
                );
                FlashMessage.flash(req, "Location updated successfully!", "success");
            } else if ("/locations/delete".equals(path)) {
                int id = Integer.parseInt(req.getParameter("id"));
                service.delete(id);
                FlashMessage.flash(req, "Location deleted successfully!", "success");
            }
        } catch (Exception e) {
            FlashMessage.flash(req, "Error: " + e.getMessage(), "danger");
        }
        resp.sendRedirect(req.getContextPath() + "/locations");
    }
}
