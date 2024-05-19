package addRecipe;

import java.sql.*;

/**
 * The AddRecipeMain class serves as the entry point for the "Add Recipe" application.
 * It establishes a connection to the database and initializes the AddRecipeController.
 */
public class AddRecipeMain {
    private static Connection connection;       // Connection object for connecting to the database

    /**
     * The main method of the AddRecipeMain class.
     * It connects to the database and initializes the AddRecipeController.
     *
     * @param args The command-line arguments (not used).
     */
    public static void main(String[] args) {
        connect();                              // Connect to the database
        new AddRecipeController(connection);    // Initialize the AddRecipeController
    }

    /**
     * Establishes a connection to the database.
     */
    private static void connect() {
        try {
            // Establish connection to the PostgreSQL database
            connection = DriverManager.getConnection("jdbc:postgresql://pgserver.mau.se:5432/drinkmaster3000",
                    "ao7503", "t360bxdp");

            // Set auto-commit to false for transaction management
            connection.setAutoCommit(false);
            System.out.println("Connection established");

        } catch (Exception e) {
            System.out.println("Error connecting controller to the database");
        }
    }
}
