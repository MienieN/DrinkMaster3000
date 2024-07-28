package addRecipe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AddRecipeGUIMain extends Application {
    private static Connection connection;
    private static AddRecipeController controller;


    public static void main(String[] args) {
        connect();
        // Initialize the AddRecipeController
        controller = new AddRecipeController(connection);
        launch();
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file for the start screen
        Parent root = FXMLLoader.load(getClass().getResource("addRecipe/gui/AddRecipeScreenManager.fxml"));
        Scene startScene = new Scene(root);
        // Set the scene for the primary stage
        primaryStage.setScene(startScene);
        // Set title for scene
        primaryStage.setTitle("Admin window - Add Recipe");
        // Display the primary stage
        primaryStage.show();
        // Locks the GUI size
        primaryStage.setResizable(false);
    }

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

    public static AddRecipeController getAddRecipeController() {
        return controller;
    }
}
