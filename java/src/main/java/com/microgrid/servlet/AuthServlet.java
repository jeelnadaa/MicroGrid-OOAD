package com.microgrid.servlet;

/**
 * This servlet handles login, signup, and logout flows.
 * It follows GRASP Controller by centralizing authentication request handling.
 * It also follows SOLID single responsibility by delegating auth rules to
 * AuthService and flash messaging to FlashMessage.
 */

import com.microgrid.model.User;
import com.microgrid.service.AuthService;
import com.microgrid.util.FlashMessage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Handles /login, /signup, /logout routes.
 */
@WebServlet(urlPatterns = {"/login", "/signup", "/logout"})
public class AuthServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();

        // If already logged in, redirect to dashboard
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            if ("/login".equals(path) || "/signup".equals(path)) {
                resp.sendRedirect(req.getContextPath() + "/");
                return;
            }
        }

        switch (path) {
            case "/login":
                req.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(req, resp);
                break;
            case "/signup":
                req.getRequestDispatcher("/WEB-INF/jsp/auth/signup.jsp").forward(req, resp);
                break;
            case "/logout":
                if (session != null) {
                    session.invalidate();
                }
                resp.sendRedirect(req.getContextPath() + "/login");
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();

        try {
            if ("/login".equals(path)) {
                handleLogin(req, resp);
            } else if ("/signup".equals(path)) {
                handleSignup(req, resp);
            }
        } catch (Exception e) {
            FlashMessage.flash(req, "An error occurred: " + e.getMessage(), "danger");
            resp.sendRedirect(req.getContextPath() + path);
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User user = authService.authenticate(username, password);

        if (user == null) {
            FlashMessage.flash(req, "Invalid username or password. Please try again.", "danger");
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (!user.isActive()) {
            FlashMessage.flash(req, "Your account has been deactivated. Please contact administrator.", "danger");
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Create session
        HttpSession session = req.getSession(true);
        session.setAttribute("user", user);
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("username", user.getUsername());

        String displayName = user.getFullName() != null ? user.getFullName() : user.getUsername();
        FlashMessage.flash(req, "Welcome back, " + displayName + "!", "success");

        resp.sendRedirect(req.getContextPath() + "/");
    }

    private void handleSignup(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirm_password");
        String fullName = req.getParameter("full_name");

        // Validation
        if (username == null || username.isBlank() ||
            email == null || email.isBlank() ||
            password == null || password.isBlank()) {
            FlashMessage.flash(req, "All fields are required.", "danger");
            resp.sendRedirect(req.getContextPath() + "/signup");
            return;
        }

        if (!password.equals(confirmPassword)) {
            FlashMessage.flash(req, "Passwords do not match.", "danger");
            resp.sendRedirect(req.getContextPath() + "/signup");
            return;
        }

        try {
            authService.register(username, email, password, confirmPassword, fullName);
            FlashMessage.flash(req, "Account created successfully! Please log in.", "success");
            resp.sendRedirect(req.getContextPath() + "/login");
        } catch (IllegalArgumentException e) {
            FlashMessage.flash(req, e.getMessage(), "danger");
            resp.sendRedirect(req.getContextPath() + "/signup");
        }
    }
}
