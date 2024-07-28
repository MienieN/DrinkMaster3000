package src.admin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.admin.boundary.AdminScreenManager;
import src.admin.controller.AdminController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AdminMain extends Application {
    // Controller for managing recipe additions
    private static AdminController adminController;
    // Connection to the database
    private static Connection connection;
    // Manager for the instruction screen
    private static AdminScreenManager adminScreen;

    /**
     * Retrieves the AdminController instance.
     *
     * @return The AdminController instance.
     */
    public static AdminController getAdminController() {
        return adminController;
    }

    /**
     * Initializes the JavaFX application.
     *
     * @param primaryStage The primary stage of the application.
     * @throws IOException if an error occurs while loading the FXML file for the start screen.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load the FXML file for the start screen
        Parent root = FXMLLoader.load(getClass().getResource("resources/AdminScreen.fxml"));
        Scene startScene = new Scene(root);
        // Set the scene for the primary stage
        primaryStage.setScene(startScene);
        // Display the primary stage
        primaryStage.show();
        // Locks the GUI size
        primaryStage.setResizable(false);
    }

    /**
     * The main method of the ClientMain class.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        connect();
        adminController = new AdminController(connection);
        adminScreen = new AdminScreenManager();
        // Launch the JavaFX application
        launch();
    }

    /**
     * Establishes a connection to the database.
     */
    public static void connect() {
        try {
            // Establish connection to the PostgreSQL database
            connection = DriverManager.getConnection("jdbc:postgresql://pgserver.mau.se:5432/drinkmaster3000",
                    "ao7503", "t360bxdp");
            System.out.println("Connection established");
        } catch (SQLException e) {
            System.out.println("Error in connection");
            throw new RuntimeException(e);
        }
    }
}
