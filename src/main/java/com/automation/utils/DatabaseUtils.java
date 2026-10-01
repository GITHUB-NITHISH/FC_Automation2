package com.automation.utils;

import java.sql.*;

/**
 * DatabaseUtils - Utility class for database operations
 */
public class DatabaseUtils {
    private static final LoggerUtils logger = LoggerUtils.getInstance();
    private static Connection connection;

    /**
     * Establish database connection
     */
    public static Connection getConnection(String host, int port, String database, 
                                          String username, String password) {
        try {
            String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
            connection = DriverManager.getConnection(url, username, password);
            logger.info("Database connection established");
            return connection;
        } catch (SQLException e) {
            logger.error("Failed to connect to database: " + e.getMessage());
            return null;
        }
    }

    /**
     * Execute SELECT query
     */
    public static ResultSet executeQuery(String query) {
        try {
            logger.info("Executing query: " + query);
            if (connection == null) {
                logger.error("Database connection is null");
                return null;
            }
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            logger.error("Query execution failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Execute UPDATE/INSERT/DELETE query
     */
    public static int executeUpdate(String query) {
        try {
            logger.info("Executing update: " + query);
            if (connection == null) {
                logger.error("Database connection is null");
                return 0;
            }
            Statement statement = connection.createStatement();
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            logger.error("Update execution failed: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Close database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Database connection closed");
            }
        } catch (SQLException e) {
            logger.error("Failed to close connection: " + e.getMessage());
        }
    }

    /**
     * Get column value from ResultSet
     */
    public static String getColumnValue(ResultSet resultSet, String columnName) {
        try {
            return resultSet.getString(columnName);
        } catch (SQLException e) {
            logger.error("Failed to get column value: " + e.getMessage());
            return null;
        }
    }
}
