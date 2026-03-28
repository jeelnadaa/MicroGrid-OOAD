package com.microgrid.model;

/**
 * Domain model for users.
 * It follows GRASP Information Expert by encapsulating user identity and status.
 * It also follows SOLID encapsulation by using private fields and public accessors.
 */

import java.time.LocalDateTime;

/**
 * Base User class for authentication.
 * Supports inheritance — Admin extends this class.
 */
public class User {
    private int userId;
    private String username;
    private String email;
    private String passwordHash;
    private String fullName;
    private String role;        // "admin" or "user"
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    public User() {}

    public User(String username, String email, String fullName, String role) {
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.active = true;
    }

    // --- Authentication methods ---

    /**
     * Login: validates the user can be authenticated (active check).
     * Password verification is handled by PasswordUtil + AuthService.
     */
    public boolean login() {
        return this.active;
    }

    /**
     * Logout: clears user session state (handled at servlet level).
     */
    public void logout() {
        // Session invalidation is handled by the servlet layer
    }

    // --- Getters & Setters ---

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    @Override
    public String toString() {
        return "User{userId=" + userId + ", username='" + username + "'}";
    }
}
