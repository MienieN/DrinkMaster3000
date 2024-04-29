package src.client.boundary;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AlcViewRecipeManager {
    private Stage stage;                                    // The stage for the scene
    private Scene scene;                                    // The scene of the GUI
    private Parent root;                                    // The root node of the scene

    // this needs added functionality to go back to the saved place we were previously, it currently
    // just resets the screen to the alc screen and you have to start over
    public void switchToAlcDrinkScreen(javafx.event.ActionEvent switchToAlcDrinkScreenButtonEvent) {
        try{
            root = FXMLLoader.load(getClass().getClassLoader().getResource("src/Client/resources/fxml/AlcDrinkScreen.fxml"));
            stage = (Stage)((Node)switchToAlcDrinkScreenButtonEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
