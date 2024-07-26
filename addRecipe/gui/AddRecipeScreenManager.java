package addRecipe.gui;

import addRecipe.AddRecipeController;
import addRecipe.AddRecipeMain;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class AddRecipeScreenManager {
    private AddRecipeController controller;

    @FXML
    private Button addRecipeButton;

    public AddRecipeScreenManager() {
        controller = AddRecipeMain.getAddRecipeController();
    }

    private void addRecipeButton() {
        //controller.addRecipe(); //complement with all the args
    }

}
