package src.client.boundary;

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
import src.client.ClientMain;
import src.client.controller.IngredientsController;
import src.client.controller.RecipeController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

/**
 * The AlcDrinkScreenManager class controls the GUI for selecting alcoholic drinks.
 */
public class AlcDrinkScreenManager implements Initializable {
    private Stage stage;                                    // The stage for the scene
    private Scene scene;                                    // The scene of the GUI
    private Parent root;                                    // The root node of the scene
    private ArrayList<String> ingredientNames;              //The list of all ingredients
    private ArrayList<String> baseDrinkNames;               // The list of base drink names //TODO needs to be just base drinks, need another array for ingredients
    private IngredientsController ingredientsController;    // The controller for managing ingredients
    private RecipeController recipeController;              // The controller for managing recipes

    @FXML
    private ComboBox<String> baseDrinkDropdownMenu;         // Dropdown menu for selecting base drinks
    @FXML
    private Button ingredientChoiceButton1;                 // Button for choosing ingredients
    @FXML
    private Button ingredientChoiceButton2;                 // Button for choosing ingredients
    @FXML
    private Button ingredientChoiceButton3;                 // Button for choosing ingredients
    @FXML
    private Button ingredientChoiceButton4;                 // Button for choosing ingredients
    @FXML
    private ListView<String> recipeList;                    // List view for displaying recipes
    @FXML
    private Button noneOfTheAboveButton;                    // Button for selecting none of the above
    private InstructionScreenManager instructionscreen;

    /**
     * Constructs a AlcDrinkScreenManager object.
     * Initializes the controllers and retrieves the list of ingredient names.
     */
    public AlcDrinkScreenManager() {
        ingredientsController = ClientMain.getIngredientsController();
        recipeController = ClientMain.getRecipeController();
        ArrayList<String> alcoholicIngredients = ingredientsController.getAlcoholicIngredientNames();
        Collections.sort(alcoholicIngredients);
        baseDrinkNames = alcoholicIngredients;
        ingredientNames = ingredientsController.getIngredientNames();
        recipeController.setAlcGUI(this);
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
        recipeController.checkForAlcRecipe(ingredientName);
        showIngredients(button);
    }

    /**
     * Handles the click event on the "None of the above" button.
     * Resets the ingredient choice buttons and shows ingredients on them.
     */
    @FXML
    private void clickNoneOfTheAboveButton() {
        ingredientNames.remove(ingredientChoiceButton1.getText());
        showIngredients(ingredientChoiceButton1);
        ingredientNames.remove(ingredientChoiceButton2.getText());
        showIngredients(ingredientChoiceButton2);
        ingredientNames.remove(ingredientChoiceButton3.getText());
        showIngredients(ingredientChoiceButton3);
        ingredientNames.remove(ingredientChoiceButton4.getText());
        showIngredients(ingredientChoiceButton4);
    }

    /**
     * Enables all ingredient choice buttons.
     */
    private void enableIngredientChoiceButtons() {
        ingredientChoiceButton1.setDisable(false);
        ingredientChoiceButton2.setDisable(false);
        ingredientChoiceButton3.setDisable(false);
        ingredientChoiceButton4.setDisable(false);
        noneOfTheAboveButton.setDisable(false);
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
     * Handles the action event of choosing a base drink from the dropdown menu.
     * Disables the dropdown menu and shows ingredients on ingredient choice buttons.
     *
     * @param chooseBaseDrink The ActionEvent object representing the selection of a base drink.
     */
    public void chooseBaseDrinkFromDropdown(javafx.event.ActionEvent chooseBaseDrink) {
        enableIngredientChoiceButtons();
        baseDrinkDropdownMenu.setDisable(true);
        String baseDrinkName = baseDrinkDropdownMenu.getValue();
        recipeController.checkForAlcRecipe(baseDrinkName);
        ingredientNames.remove(baseDrinkName);
        showIngredients(ingredientChoiceButton1);
        showIngredients(ingredientChoiceButton2);
        showIngredients(ingredientChoiceButton3);
        showIngredients(ingredientChoiceButton4);
    }

    /**
     * Switches the scene to the start screen.
     *
     * @param backToStartButtonEvent The ActionEvent object representing the click event on the back button.
     */
    public void switchToStartScreen(javafx.event.ActionEvent backToStartButtonEvent) {
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("src/Client/resources/fxml/StartScreen.fxml"));
            stage = (Stage)((Node)backToStartButtonEvent.getSource()).getScene().getWindow();
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
     * @param url            The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        baseDrinkDropdownMenu.getItems().addAll(baseDrinkNames);                    // Add base drink names to the dropdown menu
        baseDrinkDropdownMenu.setOnAction(this::chooseBaseDrinkFromDropdown);       // Set action event handler for the dropdown menu

        // Add a listener to the recipe list view for handling recipe selection changes
        recipeList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
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
        recipeController.getRecipeInstructionsForChosenAlcRecipe();
    }

    /**
     * Receives the names of all viable recipes and displays them to the user.
     *
     * @param recipeName The names of the recipe to be added to the list.
     */
    public void receiveRecipeName(String recipeName) {
        recipeList.getItems().addAll(recipeName);
    }

    /**
     * Gets the name of the selected recipe for viewing the recipe.
     * @return The name of the selected recipe.
     */
    public String getSelectedRecipeNameForViewingRecipe() {
        return recipeList.getSelectionModel().getSelectedItem();
    }

    /**
     * Opens the instruction screen.
     * @param openHelpScreen The ActionEvent object representing the click event on the help button.
     */
    @FXML
    private void startInstructions(javafx.event.ActionEvent openHelpScreen) {
        instructionscreen = new InstructionScreenManager();
        instructionscreen.openHelpWindow();
    }
}
