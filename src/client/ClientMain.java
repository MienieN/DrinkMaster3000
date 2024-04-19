package src.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.client.controller.IngredientsController;
import src.client.controller.RecipeController;

import java.io.IOException;

/**
 * The ClientMain class is the entry point of the application.
 */
//TODO: check how to correctly remove "static" from methods and variables whilst
// keeping the connection to the javafx stuff
public class ClientMain extends Application {
    private static IngredientsController ingredientsController;     // Controller for managing ingredients
    private static RecipeController recipeController;               // Controller for managing recipes

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
        primaryStage.setTitle("DrinkMaster 3000");
        // Display the primary stage
        primaryStage.show();
    }

    /**
     * The main method of the ClientMain class.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        ingredientsController = new IngredientsController();
        recipeController = new RecipeController();
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
}