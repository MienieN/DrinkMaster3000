package src.admin.boundary;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import src.admin.AdminMain;
import src.admin.controller.AdminController;
import src.client.ClientMain;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class AdminScreenManager implements Initializable {
    // Controller for managing recipe additions
    private AdminController adminController;
    // The stage for the scene
    private Stage stage;
    // The scene of the GUI
    private Scene scene;
    // The root node of the scene
    private Parent root;

    @FXML
    private TextField recipeNameTextField;
    @FXML
    private ComboBox<String> recipeNameSuggestions;
    @FXML
    private TextField recipeInstructionsTextField;
    @FXML
    private CheckBox specialityCheckbox;
    @FXML
    private GridPane inputGridPane;
    @FXML
    private Button addRecipeButton;

    @FXML
    private Button testButton;


    /**
     * Constructs a AlcDrinkScreenManager object.
     * Initializes the controllers and retrieves the list of ingredient names.
     */
    public AdminScreenManager() {
        adminController = AdminMain.getAdminController();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        recipeNameTextField.textProperty().addListener((observable, oldValue, newValue) -> updateSuggestions());

        // Dynamically create input fields for ingredients
        for (int i = 0; i < 9; i++) {
            int index = i;
            TextField ingredientNameTextField = new TextField();
            ingredientNameTextField.setPromptText("Ingredient name");
            ingredientNameTextField.textProperty().addListener((observable, oldValue, newValue) ->
                    updateIngredientSuggestions(index));

            ComboBox<String> ingredientComboBox = new ComboBox<>();
            ingredientComboBox.setEditable(true);

            CheckBox alcoholicIngredientCheckBox = new CheckBox("Alcoholic?");

            inputGridPane.add(ingredientNameTextField, 0, i);
            inputGridPane.add(ingredientComboBox, 1, i);
            inputGridPane.add(alcoholicIngredientCheckBox, 2, i);
        }
    }

    private void updateSuggestions() {
        String searchText = recipeNameTextField.getText().trim();
        recipeNameSuggestions.getItems().clear();

        if (!searchText.isEmpty()) {
            List<String> suggestions = adminController.queryRecipeName(searchText);
            recipeNameSuggestions.getItems().addAll(suggestions);
        }
    }

    private void updateIngredientSuggestions(int index) {
        TextField ingredientNameTextField = (TextField) getNodeFromGridPane(inputGridPane, 0, index);
        ComboBox<String> ingredientComboBox = (ComboBox<String>) getNodeFromGridPane(inputGridPane, 1, index);

        String searchText = ingredientNameTextField.getText().trim();
        ingredientComboBox.getItems().clear();

        if (!searchText.isEmpty()) {
            List<String> suggestions = adminController.queryIngredientsName(searchText);
            ingredientComboBox.getItems().addAll(suggestions);
        }
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    @FXML
    private void addRecipeToDatabase() {
        String name = recipeNameTextField.getText().trim();
        String instructions = recipeInstructionsTextField.getText().trim();
        boolean speciality = specialityCheckbox.isSelected();

        if (name.isEmpty()) {
            System.out.println("Recipe name is empty");
            return;
        }

        HashMap<String, Boolean> ingredients = new HashMap<>();
        for (int i = 0; i < 12; i++) {
            TextField ingredientNameTextField = (TextField) getNodeFromGridPane(inputGridPane, 0, i);
            CheckBox alcoholicIngredientCheckBox = (CheckBox) getNodeFromGridPane(inputGridPane, 2, i);
            String ingredientName = ingredientNameTextField.getText().trim();
            if (!ingredientName.isEmpty()) {
                boolean isAlcoholic = alcoholicIngredientCheckBox.isSelected();
                ingredients.put(ingredientName, isAlcoholic);
            }
        }

        adminController.addRecipe(name, ingredients, instructions, speciality);
    }

    @FXML
    private void testAddRecipe() {
        String name = recipeNameTextField.getText().trim();
        String instructions = recipeInstructionsTextField.getText().trim();
        boolean speciality = specialityCheckbox.isSelected();

        if (name.isEmpty()) {
            System.out.println("Recipe name is empty");
            return;
        }

        HashMap<String, Boolean> ingredients = new HashMap<>();
        for (int i = 0; i < 12; i++) {
            TextField ingredientNameTextField = (TextField) getNodeFromGridPane(inputGridPane, 0, i);
            CheckBox alcoholicIngredientCheckBox = (CheckBox) getNodeFromGridPane(inputGridPane, 2, i);
            String ingredientName = ingredientNameTextField.getText().trim();
            if (!ingredientName.isEmpty()) {
                boolean isAlcoholic = alcoholicIngredientCheckBox.isSelected();
                ingredients.put(ingredientName, isAlcoholic);
            }
        }

        System.out.println("Recipe Name: " + name);
        System.out.println("Speciality: " + speciality);
        System.out.println("Instructions: " + instructions);
        System.out.println("Ingredients:");
        ingredients.forEach((ingredient, isAlcoholic) ->
                System.out.println(" - " + ingredient + " (Alcoholic: " + isAlcoholic + ")"));    }
}
