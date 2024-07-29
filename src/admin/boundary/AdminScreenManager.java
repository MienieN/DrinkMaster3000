package src.admin.boundary;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import src.admin.AdminMain;
import src.admin.controller.AdminController;

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
    private ContextMenu recipeNameSuggestions;
    @FXML
    private ContextMenu ingredientNameSuggestions;
    @FXML
    private TextField recipeInstructionsTextField;
    @FXML
    private CheckBox specialityCheckbox;
    @FXML
    private GridPane inputGridPane;
    @FXML
    private VBox ingredientVBox;
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
        recipeNameSuggestions = new ContextMenu();
        recipeNameTextField.textProperty().addListener((observable, oldValue, newValue) -> updateRecipeSuggestions());


        //GridPane inputGridPane = new GridPane();

        // Dynamically create input fields for ingredients
        for (int i = 0; i < 8; i++) {
            //int index = i;
            HBox ingredientRow = new HBox();
            TextField ingredientNameTextField = new TextField();
            CheckBox ingredientCheckbox = new CheckBox("Alcoholic?");
            ContextMenu ingredientNameSuggestions = new ContextMenu();
            ingredientNameTextField.textProperty().addListener((observable, oldValue, newValue) ->
            {
                System.out.println("Text changed:" + newValue);
                handleIngredientTextChange(ingredientNameSuggestions, ingredientNameTextField);
                //updateIngredientSuggestions(index, ingredientNameSuggestions);
            });

            ingredientRow.getChildren().addAll(ingredientNameTextField, ingredientCheckbox);
            ingredientVBox.getChildren().add(ingredientRow);

            //ComboBox<String> ingredientComboBox = new ComboBox<>();
            //ingredientComboBox.setEditable(true);

            //CheckBox alcoholicIngredientCheckBox = new CheckBox("Alcoholic?");

            //inputGridPane.add(ingredientNameTextField, 0, i);
            //inputGridPane.add(alcoholicIngredientCheckBox, 1, i);

            //debugging
            //System.out.println("added textfield and checkboc at row " + i);
        }
    }

    private void updateRecipeSuggestions() {
        String searchText = recipeNameTextField.getText().trim();
        recipeNameSuggestions.getItems().clear();

        if (!searchText.isEmpty()) {
            List<String> suggestions = adminController.queryRecipeName(searchText);
            for(String suggestion : suggestions) {
                MenuItem item = new MenuItem(suggestion);
                item.setOnAction(event -> recipeNameTextField.setText(suggestion));
                recipeNameSuggestions.getItems().add(item);
            }
            //recipeNameSuggestions.getItems().addAll(suggestions);
            recipeNameSuggestions.show(recipeInstructionsTextField, Side.BOTTOM, 0,0);
        }
        else {
            recipeNameSuggestions.hide();
        }
    }

    private void handleIngredientTextChange(ContextMenu ingredientNameSuggestions, TextField ingredientNameTextField) {
        PauseTransition pause = new PauseTransition(Duration.millis(300));
        pause.setOnFinished(event -> {
            String searchText = ingredientNameTextField.getText().trim();
            //debugging
            System.out.println("search text: " + searchText);
            updateIngredientSuggestions(ingredientNameSuggestions, searchText, ingredientNameTextField);
        });
        pause.playFromStart();
    }

    private void updateIngredientSuggestions(ContextMenu ingredientNameSuggestions, String searchText, TextField ingredientNameTextField) {//int index, ContextMenu ingredientNameSuggestions) {
        //TextField ingredientNameTextField = (TextField) getNodeFromGridPane(inputGridPane, 0, index);
        //ComboBox<String> ingredientComboBox = (ComboBox<String>) getNodeFromGridPane(inputGridPane, 1, index);

        //pause.setOnFinished(event -> {

        //searchText = ingredientNameTextField.getText().trim();

        ingredientNameSuggestions.getItems().clear();
        //debugging
        System.out.println("suggestions have been reset");
        System.out.println("search text: " + searchText);


        if (!searchText.isEmpty()) {
            //debugging
            System.out.println("searching for " + searchText);
            List<String> suggestions = adminController.queryIngredientsName(searchText);
            System.out.println("suggestions: " + suggestions);
            //ingredientComboBox.getItems().addAll(suggestions);

            for(String suggestion : suggestions) {
                MenuItem item = new MenuItem(suggestion);
                item.setOnAction(happen -> ingredientNameTextField.setText(suggestion));
                ingredientNameSuggestions.getItems().add(item);
            }
            ingredientNameSuggestions.show(ingredientNameTextField, Side.RIGHT, 0,0);
            System.out.println("the suggestions are somewhere");
        }
        else {
            System.out.println("search text is empty, hiding suggestions");
            ingredientNameSuggestions.hide();
        }

    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            Integer colIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);

            // Default to zero if the index is null
            if (colIndex == null) colIndex = 0;
            if (rowIndex == null) rowIndex = 0;

            // Match the column and row indices
            if (colIndex == col && rowIndex == row) {
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
        for (Node node : ingredientVBox.getChildren()) {
            if (node instanceof HBox) {
                HBox ingredientRow = (HBox) node;
                TextField ingredientNameTextField = (TextField) ingredientRow.getChildren().get(0);
                CheckBox alcoholicCheckBox = (CheckBox) ingredientRow.getChildren().get(1);

                if (ingredientNameTextField != null) {
                    String ingredientName = ingredientNameTextField.getText().trim();
                    System.out.println(ingredientName);

                    if (!ingredientName.isEmpty()) {
                        if (alcoholicCheckBox != null) {
                            boolean isAlcoholic = alcoholicCheckBox.isSelected();
                            ingredients.put(ingredientName, isAlcoholic);
                            System.out.println("added " + ingredientName + " has alcohol? " + isAlcoholic);
                        }
                    }
                    else {
                        System.out.println("skipped empty field");
                    }
                }
                else {
                    System.out.println("skipped missing field");
                }
            }
        }

        /*
        for (int i = 0; i < 9; i++) {
            TextField ingredientNameTextField = (TextField) getNodeFromGridPane(inputGridPane, 0, i);
            CheckBox alcoholicIngredientCheckBox = (CheckBox) getNodeFromGridPane(inputGridPane, 1, i);

            if(ingredientNameTextField != null ) {
                String ingredientName = ingredientNameTextField.getText().trim();
                System.out.println(ingredientName);
                if (!ingredientName.isEmpty()) {
                    if(alcoholicIngredientCheckBox != null) {
                        boolean isAlcoholic = alcoholicIngredientCheckBox.isSelected();
                        ingredients.put(ingredientName, isAlcoholic);
                        System.out.println("added " + ingredientName + " has value: " + isAlcoholic);
                    }
                }
                else {
                    System.out.println("skipped empty field at row: " + i);
                }
            }
            else {
                System.out.println("skipped missing field at row: " + i);
            }

        }

         */

        System.out.println("Recipe Name: " + name);
        System.out.println("Speciality: " + speciality);
        System.out.println("Instructions: " + instructions);
        System.out.println("Ingredients:");



        if (ingredients.isEmpty()) {
            System.out.println("No ingredients found");
        } else {
            ingredients.forEach((ingredient, isAlcoholic) ->
                    System.out.println(" - " + ingredient + " (Alcoholic: " + isAlcoholic + ")"));
        }
    }
}
