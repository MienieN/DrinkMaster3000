package src.Client.Boundary.guiClasses;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import src.Client.ClientMain;
import src.Client.Controller.IngredientsController;
import src.Client.Controller.RecipeController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GUIAlcDrinkScreenController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private ArrayList<String> baseDrinkNames; //TODO needs to be just base drinks, need another array for ingredients
    private IngredientsController ingredientsController;
    private RecipeController recipeController;
    private int counter = 0;

    @FXML
    private ComboBox<String> baseDrinkDropdownMenu;
    @FXML
    private Button ingredientChoiceButton1;
    @FXML
    private Button ingredientChoiceButton2;
    @FXML
    private Button ingredientChoiceButton3;
    @FXML
    private Button ingredientChoiceButton4;
    @FXML
    private ListView<String> recipeList;


    public GUIAlcDrinkScreenController() {
        ingredientsController = ClientMain.getIngredientsController();
        recipeController = ClientMain.getRecipeController();
        baseDrinkNames = ingredientsController.getIngredientNames();
        recipeController.setGUI(this);
    }

    @FXML
    private void clickIngredientChoiceButton(ActionEvent event){
        Button button = (Button) event.getSource();
        String ingredientName = button.getText();
        baseDrinkNames.remove(ingredientName);
        recipeController.checkForRecipe(ingredientName);
        showIngredients(button);
    }

    private void enableIngredientChoiceButtons(){
        ingredientChoiceButton1.setDisable(false);
        ingredientChoiceButton2.setDisable(false);
        ingredientChoiceButton3.setDisable(false);
        ingredientChoiceButton4.setDisable(false);
    }

    private void showIngredients(Button button){
        if(!(counter >= baseDrinkNames.size())){
            button.setText(baseDrinkNames.get(counter++));
        }
    }

    public void chooseBaseDrinkFromDropdown(javafx.event.ActionEvent chooseBaseDrink) {
        enableIngredientChoiceButtons();
        baseDrinkDropdownMenu.setDisable(true);
        String baseDrinkName = baseDrinkDropdownMenu.getValue();
        recipeController.checkForRecipe(baseDrinkName);
        baseDrinkNames.remove(baseDrinkName);
        showIngredients(ingredientChoiceButton1);
        showIngredients(ingredientChoiceButton2);
        showIngredients(ingredientChoiceButton3);
        showIngredients(ingredientChoiceButton4);
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
        baseDrinkDropdownMenu.setOnAction(this::chooseBaseDrinkFromDropdown);

        recipeList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                popupRecipe();
            }
        });

    }

    private void popupRecipe() {
        System.out.println("testtestetteat");
    }

    public void receiveRecipeName(String recipeName) {
        recipeList.getItems().add(recipeName);
    }
}
