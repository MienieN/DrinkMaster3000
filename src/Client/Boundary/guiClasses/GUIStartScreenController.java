package src.Client.Boundary.guiClasses;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIStartScreenController{
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToAlcDrinkScreen(javafx.event.ActionEvent drinksButtonEvent) {
        try{
            root = FXMLLoader.load(getClass().getResource("/fxml/AlcDrinkScreen.fxml"));
            stage = (Stage)((Node)drinksButtonEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
