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

public class AdminScreenManager implements Initializable {
    // Controller for managing recipe additions
     private AdminController adminController;

    @FXML
    private TextField recipeNameTextField;
    @FXML
    private ContextMenu recipeNameSuggestions;
    @FXML
    private TextField recipeInstructionsTextField;
    @FXML
    private CheckBox specialityCheckbox;
    @FXML
    private VBox ingredientVBox;

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
