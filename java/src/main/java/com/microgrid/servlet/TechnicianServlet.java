package com.microgrid.servlet;

/**
 * This servlet controls technician management workflows.
 * It follows GRASP Controller by directing requests to TechnicianService.
 * It keeps its own responsibility focused on web handling.
 */

import com.microgrid.model.Technician;
import com.microgrid.service.TechnicianService;
import com.microgrid.util.FlashMessage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for Technician CRUD: /technicians/*
 */
@WebServlet(urlPatterns = {"/technicians", "/technicians/create", "/technicians/edit", "/technicians/delete"})
public class TechnicianServlet extends HttpServlet {

    private final TechnicianService service = new TechnicianService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();
        try {
            if ("/technicians/create".equals(path)) {
                req.getRequestDispatcher("/WEB-INF/jsp/technicians/form.jsp").forward(req, resp);
            } else if ("/technicians/edit".equals(path)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Technician tech = service.getById(id);
                if (tech == null) { resp.sendError(404); return; }
                req.setAttribute("technician", tech);
                req.getRequestDispatcher("/WEB-INF/jsp/technicians/form.jsp").forward(req, resp);
            } else {
                String search = req.getParameter("search");
                List<Technician> technicians = service.search(search);

                // Build tech stats: (technician, maintenance_count)
                List<Object[]> techStats = new ArrayList<>();
                for (Technician t : technicians) {
                    int count = service.getMaintenanceCount(t.getTechId());
                    techStats.add(new Object[]{t, count});
                }

                req.setAttribute("techStats", techStats);
                req.setAttribute("search", search != null ? search : "");
                req.getRequestDispatcher("/WEB-INF/jsp/technicians/list.jsp").forward(req, resp);
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
            if ("/technicians/create".equals(path)) {
                service.create(
                    req.getParameter("name"),
                    req.getParameter("contact_no"),
                    req.getParameter("specialization")
                );
                FlashMessage.flash(req, "Technician created successfully!", "success");
            } else if ("/technicians/edit".equals(path)) {
                int id = Integer.parseInt(req.getParameter("id"));
                service.update(id,
                    req.getParameter("name"),
                    req.getParameter("contact_no"),
                    req.getParameter("specialization")
                );
                FlashMessage.flash(req, "Technician updated successfully!", "success");
            } else if ("/technicians/delete".equals(path)) {
                int id = Integer.parseInt(req.getParameter("id"));
                service.delete(id);
                FlashMessage.flash(req, "Technician deleted successfully!", "success");
            }
        } catch (Exception e) {
            FlashMessage.flash(req, "Error: " + e.getMessage(), "danger");
        }
        resp.sendRedirect(req.getContextPath() + "/technicians");
    }
}
