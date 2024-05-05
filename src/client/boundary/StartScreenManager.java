package src.client.boundary;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


/**
 * The StartScreenManager class manages the navigation from the start screen to other screens in the application.
 */
public class StartScreenManager {
    private Stage stage;        // The stage for the scene
    private Scene scene;        // The scene of the GUI
    private Parent root;        // The root node of the scene

    /**
     * Switches the scene to the alcoholic drink selection screen.
     *
     * @param drinksButtonEvent The ActionEvent object representing the click event on the drinks button.
     */
    public void switchToAlcDrinkScreen(javafx.event.ActionEvent drinksButtonEvent) {
        try{
            // Load the FXML file for the alcoholic drink selection screen
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("src/Client/resources/fxml/AlcDrinkScreen.fxml")));
            // Get the stage from the source of the event
            stage = (Stage)((Node)drinksButtonEvent.getSource()).getScene().getWindow();
            // Create a new scene with the loaded FXML content
            scene = new Scene(root);
            // Set the scene of the stage
            stage.setScene(scene);
            // Show the stage
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void switchToNonAlcDrinkScreen(javafx.event.ActionEvent nonAlcDrinkEvent) {
        try{
            // Load the FXML file for the alcoholic drink selection screen
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("src/client/resources/fxml/NonAlcDrinkScreen.fxml")));
            // Get the stage from the source of the event
            stage = (Stage)((Node)nonAlcDrinkEvent.getSource()).getScene().getWindow();
            // Create a new scene with the loaded FXML content
            scene = new Scene(root);
            // Set the scene of the stage
            stage.setScene(scene);
            // Show the stage
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
