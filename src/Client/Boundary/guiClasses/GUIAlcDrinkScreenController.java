package src.Client.Boundary.guiClasses;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIAlcDrinkScreenController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void chooseBaseDrinkFromDropdown(javafx.event.ActionEvent chooseBaseDrink) {
        try{
            root = FXMLLoader.load(getClass().getResource("Boundary/resources/fxml/AlcDrinkScreen.fxml"));
            stage = (Stage)((Node)chooseBaseDrink.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void switchToStartScreen(javafx.event.ActionEvent backToStartButtonEvent) {
        try{
            root = FXMLLoader.load(getClass().getClassLoader().getResource("src/Client/resources/fxml/StartScreen.fxml"));
            stage = (Stage)((Node)backToStartButtonEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
