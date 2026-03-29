package com.microgrid.dao;

/**
 * This file manages JDBC database connection creation and configuration.
 * It centralizes the creation of Connection objects and keeps database
 * access details in one responsible place, following GRASP Controller
 * and Single Responsibility from SOLID. The static initialization is a
 * simple creational style that prevents other classes from duplicating
 * connection setup logic.
 */

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton database connection manager using JDBC.
 */
public class DatabaseConnection {

    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Load properties file as base defaults
            Properties props = new Properties();
            InputStream is = DatabaseConnection.class.getClassLoader()
                    .getResourceAsStream("db.properties");
            if (is != null) {
                props.load(is);
            }

            // Environment variables (from Docker) take priority over db.properties
            String host = env("DB_HOST", props.getProperty("db.host", "localhost"));
            String port = env("DB_PORT", props.getProperty("db.port", "3306"));
            String dbName = env("DB_NAME", props.getProperty("db.name", "microclimate_grid"));
            URL = "jdbc:mysql://" + host + ":" + port + "/" + dbName +
                  "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            USER = env("DB_USER", props.getProperty("db.user", "root"));
            PASSWORD = env("DB_PASSWORD", props.getProperty("db.password", ""));
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database connection", e);
        }
    }

    /** Return environment variable if set, otherwise the fallback value. */
    private static String env(String name, String fallback) {
        String val = System.getenv(name);
        return (val != null && !val.isEmpty()) ? val : fallback;
    }

    private DatabaseConnection() {} // Prevent instantiation

    /**
     * Get a new database connection.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Safely close a connection.
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
