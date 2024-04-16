package src.Client.Boundary.guiClasses;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import src.Client.ClientMain;
import src.Client.Controller.IngredientsController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GUIAlcDrinkScreenController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ComboBox<String> baseDrinkDropdownMenu;
    private ArrayList<String> baseDrinkNames;
    private IngredientsController ingredientsController;


    public GUIAlcDrinkScreenController() {
        ingredientsController = ClientMain.getIngredientsController();
        baseDrinkNames = ingredientsController.getIngredientNames();
    }

    public void chooseBaseDrinkFromDropdown(javafx.event.ActionEvent chooseBaseDrink) {

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        baseDrinkDropdownMenu.getItems().addAll(baseDrinkNames);

    }
}
