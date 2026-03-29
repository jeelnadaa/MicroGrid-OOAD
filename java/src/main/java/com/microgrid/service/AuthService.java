package com.microgrid.service;

/**
 * This service manages authentication and registration behavior.
 * It follows SOLID single responsibility by keeping auth rules separate
 * from servlet request handling and persistence. It also follows GRASP
 * Information Expert by delegating credential lookup to UserDAO and
 * password checking to PasswordUtil.
 */

import com.microgrid.dao.UserDAO;
import com.microgrid.model.User;
import com.microgrid.util.PasswordUtil;

import java.sql.SQLException;

/**
 * Service layer for authentication operations.
 */
public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    /**
     * Authenticate a user by username and password.
     * @return the User if authentication succeeds, null otherwise.
     */
    public User authenticate(String username, String password) throws SQLException {
        User user = userDAO.findByUsername(username);
        if (user == null) return null;

        if (!PasswordUtil.checkPassword(password, user.getPasswordHash())) {
            return null;
        }

        if (!user.isActive()) {
            return null;
        }

        // Update last login
        userDAO.updateLastLogin(user.getUserId());
        return user;
    }

    /**
     * Register a new user.
     * @return error message if registration fails, null on success.
     */
    public String register(String username, String email, String password,
                           String confirmPassword, String fullName) throws SQLException {
        if (username == null || username.isBlank() ||
            email == null || email.isBlank() ||
            password == null || password.isBlank()) {
            return "All fields are required.";
        }

        if (!password.equals(confirmPassword)) {
            return "Passwords do not match.";
        }

        if (password.length() < 6) {
            return "Password must be at least 6 characters long.";
        }

        if (userDAO.findByUsername(username) != null) {
            return "Username already exists. Please choose another.";
        }

        if (userDAO.findByEmail(email) != null) {
            return "Email already registered. Please use another or log in.";
        }

        User user = new User(username, email, fullName, "user");
        user.setPasswordHash(PasswordUtil.hashPassword(password));
        user.setActive(true);
        userDAO.save(user);
        return null; // success
    }
}
