package addRecipe;

import java.sql.*;

/**
 * The AddRecipeMain class serves as the entry point for the "Add Recipe" application.
 * It establishes a connection to the database and initializes the AddRecipeController.
 */
public class AddRecipeMain {
    // Connection object for connecting to the database
    private static Connection connection;

    /**
     * The main method of the AddRecipeMain class.
     * It connects to the database and initializes the AddRecipeController.
     *
     * @param args The command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Connect to the database
        connect();
        // Initialize the AddRecipeController
        new AddRecipeController(connection);
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
