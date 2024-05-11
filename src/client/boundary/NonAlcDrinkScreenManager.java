package src.client.boundary;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import src.client.ClientMain;
import src.client.controller.IngredientsController;
import src.client.controller.RecipeController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class NonAlcDrinkScreenManager {
    private Stage stage;                                    // The stage for the scene
    private Scene scene;                                    // The scene of the GUI
    private Parent root;                                    // The root node of the scene
    private ArrayList<String> ingredientNames;              //The list of all ingredients
    private IngredientsController ingredientsController;    // The controller for managing ingredients
    private RecipeController recipeController;              // The controller for managing recipes
    private InstructionScreenManager instructionScreen;

    @FXML
    private Button ingredientChoiceButton1;                 // Button for choosing ingredients
    @FXML
    private Button ingredientChoiceButton2;                 // Button for choosing ingredients
    @FXML
    private Button ingredientChoiceButton3;                 // Button for choosing ingredients
    @FXML
    private Button ingredientChoiceButton4;                 // Button for choosing ingredients
    @FXML
    private ListView<String> matchList;                    // List view for displaying recipes

    /**
     * Constructs a AlcDrinkScreenManager object.
     * Initializes the controllers and retrieves the list of ingredient names.
     */
    public NonAlcDrinkScreenManager() {
        ingredientsController = ClientMain.getIngredientsController();
        recipeController = ClientMain.getRecipeController();
        ArrayList<String> nonAlcoholicIngredients = ingredientsController.getNonAlcoholicIngredientNames();
        Collections.sort(nonAlcoholicIngredients);
        ingredientNames = ingredientsController.getNonAlcoholicIngredientNames();
        recipeController.setNonAlcGUI(this);
    }

    /**
     * Handles the click event on ingredient choice buttons.
     * Retrieves the ingredient name from the clicked button and checks for recipes containing that ingredient.
     *
     * @param event The ActionEvent object representing the click event.
     */
    @FXML
    private void clickIngredientChoiceButton(ActionEvent event) {
        Button button = (Button) event.getSource();
        String ingredientName = button.getText();
        recipeController.checkForNonAlcRecipe(ingredientName);
        showIngredients(button);
    }

    /**
     * Handles the click event on the "None of the above" button.
     * Resets the ingredient choice buttons and shows ingredients on them.
     *
     */
    @FXML
    private void clickNoneOfTheAboveButton() {
        showIngredients(ingredientChoiceButton1);
        ingredientNames.remove(ingredientChoiceButton2.getText());
        showIngredients(ingredientChoiceButton2);
        ingredientNames.remove(ingredientChoiceButton3.getText());
        showIngredients(ingredientChoiceButton3);
        ingredientNames.remove(ingredientChoiceButton4.getText());
        showIngredients(ingredientChoiceButton4);
    }

    public void putIngredientNamesOnChoiceButtonsOnScreenChange() {
        showIngredients(ingredientChoiceButton1);
        showIngredients(ingredientChoiceButton2);
        showIngredients(ingredientChoiceButton3);
        showIngredients(ingredientChoiceButton4);
    }

    /**
     * Displays ingredients on the ingredient choice buttons.
     *
     * @param button The button to display the ingredient on.
     */
    private void showIngredients(Button button) {
        if (!(ingredientNames.isEmpty())) {
            String temp = ingredientNames.get(0);
            button.setText(temp);
            ingredientNames.remove(temp);

        } else {
            button.setText("End");
            button.setDisable(true);
        }
    }

    /**
     * Switches the scene to the start screen.
     *
     * @param backToStartButtonEvent The ActionEvent object representing the click event on the back button.
     */
    public void switchToStartScreen(javafx.event.ActionEvent backToStartButtonEvent) {
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("src/Client/resources/fxml/StartScreen.fxml"));
            stage = (Stage) ((Node) backToStartButtonEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            recipeController.resetChosenIngredients();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initializes the GUI components and event handlers when the GUI is loaded.
     * Adds base drink names to the dropdown menu, sets an action event handler for the dropdown menu,
     * and adds a listener to the recipe list view for handling recipe selection changes.
     *
     */
    public void initialize() {
        putIngredientNamesOnChoiceButtonsOnScreenChange();
        // Add a listener to the recipe list view for handling recipe selection changes
        matchList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {

                // Call the method to display a popup with the selected recipe
                popupRecipe();
            }
        });
    }

    /**
     * Displays a popup with the selected recipe.
     */
    private void popupRecipe() {
        recipeController.getRecipeInstructionsForChosenNonAlcRecipe();
    }

    /**
     * Receives the names of all viable recipes and displays them to the user.
     *
     * @param recipeNames The names of the recipes to be added to the list.
     */
    public void receiveMatches(ArrayList<String> recipeNames) {
        matchList.getItems().clear();
        matchList.getItems().addAll(recipeNames);
    }

    @FXML
    private void startInstructions(javafx.event.ActionEvent openHelpScreen){
        if (instructionScreen == null){
            instructionScreen = ClientMain.getInstructionscreen();
        }
        instructionScreen.openHelpWindow();
    }

    public String getSelectedRecipeNameForViewingRecipe() {
        return matchList.getSelectionModel().getSelectedItem();
    }
}
