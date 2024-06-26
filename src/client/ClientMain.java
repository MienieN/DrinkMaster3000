package src.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.client.controller.IngredientsController;
import src.client.controller.RecipeController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class that is the entry point of the application.
 */
//TODO: check how to correctly remove "static" from methods and variables whilst
// keeping the connection to the javafx stuff
public class ClientMain extends Application {
    private static IngredientsController ingredientsController;     // Controller for managing ingredients
    private static RecipeController recipeController;               // Controller for managing recipes
    private static Connection connection;                           // Connection to the database

    /**
     * Retrieves the RecipeController instance.
     *
     * @return The RecipeController instance.
     */
    public static RecipeController getRecipeController() {
        return recipeController;
    }

    /**
     * Initializes the JavaFX application.
     *
     * @param primaryStage The primary stage of the application.
     * @throws IOException if an error occurs while loading the FXML file for the start screen.
     */
    //TODO: add database connections here instead of in their own controllers
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load the FXML file for the start screen
        Parent root = FXMLLoader.load(getClass().getResource("resources/fxml/StartScreen.fxml"));
        Scene startScene = new Scene(root);
        // Set the scene for the primary stage
        primaryStage.setScene(startScene);
        //primaryStage.setTitle("DrinkMaster 3000");
        // Display the primary stage
        primaryStage.show();
    }

    /**
     * The main method of the ClientMain class.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        connect();
        ingredientsController = new IngredientsController(connection);
        recipeController = new RecipeController(connection);
        recipeController.setIngredientsController(ingredientsController);
        // Launch the JavaFX application
        launch();
    }

    /**
     * Retrieves the IngredientsController instance.
     *
     * @return The IngredientsController instance.
     */
    public static IngredientsController getIngredientsController(){
        return ingredientsController;
    }

    /**
     * Establishes a connection to the database.
     */
    //TODO move this into main and send the connection to the controllers
    public static void connect() {
        try {
            // Establish connection to the PostgreSQL database
            connection = DriverManager.getConnection("jdbc:postgresql://pgserver.mau.se:5432/drinkmaster3000", "ao7503", "t360bxdp");
            System.out.println("Connection established");
        } catch (SQLException e) {
            System.out.println("Error in connection");
            throw new RuntimeException(e);
        }
    }
}