package src.admin.boundary;

import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import src.admin.AdminMain;
import src.admin.controller.AdminController;

import java.net.URL;
import java.util.*;

/**
 * The AdminScreenManager class is responsible for managing the user interface for adding recipes.
 * It provides methods for handling user interactions and database operations related to recipe management.
 */

public class AdminScreenManager implements Initializable {
    // Controller for managing recipe additions
     private AdminController adminController;

    // Text field for recipe names
    @FXML
    private TextField recipeNameTextField;
    // A context menu to store suggested recipe names when typing
    @FXML
    private ContextMenu recipeNameSuggestions;
    // Text field for instruction input
    @FXML
    private TextField recipeInstructionsTextField;
    // Checkbox with boolean for speciality indicator
    @FXML
    private CheckBox specialityCheckbox;
    // A VBox for ingredient text fields
    @FXML
    private VBox ingredientVBox;

    /**
     * Constructs an AdminScreenManager object.
     * Initializes the controllers and retrieves the list of ingredient and recipe names.
     */
    public AdminScreenManager() {
        adminController = AdminMain.getAdminController();
    }

    /**
     * Initializes the controller class.
     * This method is automatically called after the FXML file has been loaded.
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        recipeNameSuggestions = new ContextMenu();
        recipeNameTextField.textProperty().addListener((observable, oldValue, newValue) -> updateRecipeSuggestions());

        // Dynamically create input fields for ingredients
        for (int i = 0; i < 10; i++) {
            HBox ingredientRow = new HBox();
            TextField ingredientNameTextField = new TextField();
            CheckBox ingredientCheckbox = new CheckBox("Alcoholic?");
            ContextMenu ingredientNameSuggestions = new ContextMenu();
            ingredientNameTextField.textProperty().addListener((observable, oldValue, newValue) ->
            {
                System.out.println("Text changed:" + newValue);
                handleIngredientTextChange(ingredientNameSuggestions, ingredientNameTextField);
            });

            ingredientRow.getChildren().addAll(ingredientNameTextField, ingredientCheckbox);
            ingredientVBox.getChildren().add(ingredientRow);
        }
    }

    /**
     * Updates the recipe suggestions in the context menu based on the text entered in the recipe name text field.
     */
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
            recipeNameSuggestions.show(recipeNameTextField, Side.RIGHT, 0,0);
        }
        else {
            recipeNameSuggestions.hide();
        }
    }

    /**
     * Handles changes to the ingredient name text field and updates suggestions based on the input.
     *
     * @param ingredientNameSuggestions The context menu to show suggestions for ingredient names.
     * @param ingredientNameTextField The text field where the user enters the ingredient name.
     */
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

    /**
     * Updates the ingredient suggestions in the context menu based on the text entered in the ingredient name text field.
     *
     * @param ingredientNameSuggestions The context menu to show suggestions for ingredient names.
     * @param searchText The text to search for matching ingredient names.
     * @param ingredientNameTextField The text field where the user enters the ingredient name.
     */
    private void updateIngredientSuggestions(ContextMenu ingredientNameSuggestions, String searchText, TextField ingredientNameTextField) {//int index, ContextMenu ingredientNameSuggestions) {
        ingredientNameSuggestions.getItems().clear();
        //debugging
        System.out.println("suggestions have been reset");
        System.out.println("search text: " + searchText);


        if (!searchText.isEmpty()) {
            //debugging
            System.out.println("searching for " + searchText);
            List<String> suggestions = adminController.queryIngredientsName(searchText);
            System.out.println("suggestions: " + suggestions);

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

    /**
     * Adds a recipe to the database by gathering input data from the UI fields.
     * This operation is performed in a background thread to avoid blocking the UI.
     */
    @FXML
    private void addRecipeToDatabase() {
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                String name = recipeNameTextField.getText().trim();
                String instructions = recipeInstructionsTextField.getText().trim();
                boolean speciality = specialityCheckbox.isSelected();

                if (name.isEmpty()) {
                    System.out.println("Recipe name is empty");
                    return null;
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
                adminController.addRecipe(name, ingredients, instructions, speciality);
                return null;
            }
        };

        task.setOnSucceeded(e -> System.out.println("Recipe added successfully"));
        task.setOnFailed(e -> System.out.println("Failed to add recipe" + task.getException().getMessage()));

        new Thread(task).start();
    }

    /**
     * Tests adding a recipe by printing the input data to the console.
     * This method is useful for debugging and verifying the input handling logic.
     */
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
