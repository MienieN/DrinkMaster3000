package src.admin.resources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The DatabaseConnection class provides a singleton pattern for managing
 * the database connection. It ensures that only one connection is opened
 * and shared across the application.
 */
public class DatabaseConnection {
    // Singleton instance of the database connection
    private static Connection connection;

    /**
     * Private constructor to prevent instantiation of the class.
     */
    private DatabaseConnection() {
        // Prevent instantiation
    }

    /**
     * Returns a Connection object to the database. If the connection is
     * not already established or is closed, a new connection is created.
     * The connection is configured to not auto-commit changes.
     *
     * @return A Connection object to the database.
     * @throws SQLException If a database access error occurs or the connection URL is incorrect.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            // Establish a connection to the PostgreSQL database
            connection = DriverManager.getConnection("jdbc:postgresql://pgserver.mau.se:5432/drinkmaster3000",
                    "ao7503", "t360bxdp");
            connection.setAutoCommit(false);
            System.out.println("Connection established");
        }
        return connection;
    }
}
