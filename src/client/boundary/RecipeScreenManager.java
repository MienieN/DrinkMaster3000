package src.client.boundary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The RecipeScreenManager class serves as the entry point for the "View Recipe" screen of the application.
 * It extends the Application class and initializes the JavaFX stage with the corresponding FXML file.
 */
public class RecipeScreenManager extends Application {

    /**
     * The main method of the GUIRecipeController class.
     * It launches the JavaFX application.
     *
     * @param args The command-line arguments (not used).
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the JavaFX application.
     *
     * @param ViewRecipeStage The primary stage for the "View Recipe" screen.
     * @throws IOException if an error occurs while loading the FXML file.
     */
    @Override
    public void start(Stage ViewRecipeStage) throws IOException {
        // Load the FXML file for the "View Recipe" screen
        Parent viewRecipeScrollPane = FXMLLoader.load(getClass().getClassLoader().getResource("src/Client/resources/fxml/ViewRecipeScreen.fxml"));
        // Set the title for the stage
        ViewRecipeStage.setTitle("DrinkMaster 3000");
        // Set the scene of the stage with the loaded FXML content
        ViewRecipeStage.setScene(new Scene(viewRecipeScrollPane));
        // Display the stage
        ViewRecipeStage.show();
    }
}

