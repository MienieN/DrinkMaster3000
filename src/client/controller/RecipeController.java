package src.client.controller;

import javafx.scene.control.Alert;
import src.client.boundary.AlcDrinkScreenManager;
import src.client.entity.Ingredient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * The RecipeController class manages the recipes and their interactions with the GUI.
 */
public class RecipeController {
    private HashMap<String, HashSet<Ingredient>> recipes = new HashMap<>(); // HashMap to store recipes and their ingredients
    private HashMap<String, String> recipeInstructions = new HashMap<>();   // HashMap to store recipes and their instructions
    private AlcDrinkScreenManager alcDrinkScreenManager;                    // GUI controller for displaying recipes
    private HashSet<Ingredient> chosenIngredients = new HashSet<>();        // List of chosen ingredients
    private Connection connection;                                          // Database connection
    private IngredientsController ingredientsController;


    /**
     * Constructs a RecipeController object and initializes the database connection.
     */
    public RecipeController(Connection connection) {
        this.connection = connection;
        getRecipesFromDatabase();
    }

    /**
     * Retrieves recipes from the database and populates the recipes HashMap with recipe names and their corresponding ingredients.
     */
    private void getRecipesFromDatabase() {
        String sql = "Select recipe_name from recipes";
        String sql2 = "select recipes_ingredients.ingredient_name, ingredients.alcoholic from recipes_ingredients " +
                "join ingredients ON recipes_ingredients.ingredient_name = ingredients.ingredient_name " +
                "WHERE recipe_name = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet recipeNames = statement.executeQuery();
            PreparedStatement statement2 = connection.prepareStatement(sql2);
            while (recipeNames.next()) {
                for (int i = 1; i <= recipeNames.getMetaData().getColumnCount(); i++) {
                    HashSet<Ingredient> ingredientHashSet = new HashSet<>();
                    String recipeName = recipeNames.getString(i);
                    statement2.setString(1, recipeName);
                    ResultSet resultSet2 = statement2.executeQuery();
                    while (resultSet2.next()) {
                        for (int j = 1; j <= resultSet2.getMetaData().getColumnCount(); j++) {
                            ingredientHashSet.add(new Ingredient(resultSet2.getString(j++), resultSet2.getBoolean(j)));
                        }
                    }
                    recipes.put(recipeName, ingredientHashSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the recipe instructions for the chosen recipe and displays them in an alert.
     */
    public void getRecipeInstructionsForChosenRecipe() {
        recipeInstructions.clear();
        //  declares an SQL query string that selects the recipe_name and instructions column from the table named recipes
        //  filtering by recipe_name. The ? is a placeholder for a parameter that will be filled in later.
        String showRecipeSQL = "SELECT recipe_name, instructions FROM recipes WHERE recipe_name = ?";

        // This line begins a try-with-resources block. It prepares a PreparedStatement object using the SQL query
        // string showRecipeSQL. The connection object is assumed to be a database connection.
        try (PreparedStatement statement = connection.prepareStatement(showRecipeSQL)) {
            // This line sets the value of the first parameter (denoted by 1) in the prepared statement.
            // It sets the selected recipe name retrieved from alcDrinkScreenManager.
            statement.setString(1, alcDrinkScreenManager.getSelectedRecipeNameForViewingRecipe());
            // This line executes the query defined in the prepared statement and stores the result in a ResultSet
            // object named resultSet.
            ResultSet resultSet = statement.executeQuery();

            // This line moves the cursor of the resultSet to the next row and checks if there is a row present.
            if(resultSet.next()) {
                // These lines retrieve the value of the recipe_name and instructions column from the current row of
                // the resultSet and stores it in a variable named recipeName.
                String recipeName = resultSet.getString("recipe_name");
                String instructions = resultSet.getString("instructions");

                // This line adds the recipeName and corresponding instructionsHashSet to a map named recipeInstructions.
                recipeInstructions.put(recipeName, instructions);

                //Pop up box
                Alert alert = new Alert(Alert.AlertType.INFORMATION); //creates an alert object
                alert.setTitle("Recipe"); //sets the title of the alert
                alert.setHeaderText(null); //sets the header text of the alert
                alert.setContentText("Recipe name: " + recipeName + "\nInstructions: " + instructions); //sets the content text of the alert
                alert.showAndWait(); //displays the alert and waits for the user to close it
            }
            //System.out.println(recipeInstructions);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if the chosen ingredients match any recipes in the database.
     * If a match is found, the recipe name is sent to the GUI controller.
     * @param chosenIngredientName The name of the chosen ingredient.
     */
    public void checkForRecipe(String chosenIngredientName) {
        ArrayList<String> recipeNames = new ArrayList();
        chosenIngredients.add(ingredientsController.getIngredientFromArrayList(chosenIngredientName));
        Iterator<Map.Entry<String, HashSet<Ingredient>>> iterator = recipes.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, HashSet<Ingredient>> entry = iterator.next();
            if (chosenIngredients.containsAll(entry.getValue())) {
                recipeNames.add(entry.getKey());
                System.out.println(recipeNames);
                alcDrinkScreenManager.receiveRecipeName(recipeNames);
                iterator.remove();
            }
        }
    }

    //TODO: add a separate method like the checkForRecipe() for non-alcoholic drinks,
    // potentially another for the speciality drinks. Mimmis note: why would we do this?

    /**
     * Sets the GUI controller for displaying recipes.
     *
     * @param GUIController The GUI controller for displaying recipes.
     */
    public void setGUI(AlcDrinkScreenManager GUIController) {
        this.alcDrinkScreenManager = GUIController;
    }

    /**
     * A setter for the {@link IngredientsController}
     *
     * @param ingredientsController the {@link IngredientsController} created in {@clientMain}
     */
    public void setIngredientsController(IngredientsController ingredientsController) {
        this.ingredientsController = ingredientsController;
    }
}