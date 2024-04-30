package src.client.controller;

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
    private HashMap<String, HashSet<Ingredient>> recipes = new HashMap<String, HashSet<Ingredient>>();              // HashMap to store recipes and their ingredients
    private HashMap<String, String> recipeInstructions = new HashMap<>();  // HashMap to store recipes and their instructions
    private AlcDrinkScreenManager GUIController;                                          // GUI controller for displaying recipes
    private HashSet<Ingredient> chosenIngredients = new HashSet<>();                      // List of chosen ingredients
    private Connection connection;                                                        // Database connection
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

    public void getRecipeInstructionsForChosenRecipe() {
        //  declares an SQL query string that selects the recipe_name and instructions column from the table named recipes.
        String showRecipeSQL = "SELECT recipe_name, instructions FROM recipes WHERE recipe_name = ?";

        // a PreparedStatement is prepared using the SQL query showRecipeSQL. The connection object is a database connection.
        try (PreparedStatement statement = connection.prepareStatement(showRecipeSQL)) {
            statement.setString(1, GUIController.getSelectedRecipeName());
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                String recipeName = resultSet.getString("recipe_name");
                String instructions = resultSet.getString("instructions");

                // This line adds the recipeName and corresponding instructionsHashSet to a map named recipeInstructions.
                recipeInstructions.put(recipeName, instructions);
            }
            System.out.println(recipeInstructions);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkForRecipe(String chosenIngredientName) {
        ArrayList<String> recipeNames = new ArrayList();
        chosenIngredients.add(ingredientsController.getIngredientFromArrayList(chosenIngredientName));
        Iterator<Map.Entry<String, HashSet<Ingredient>>> iterator = recipes.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, HashSet<Ingredient>> entry = iterator.next();
            if (chosenIngredients.containsAll(entry.getValue())) {
                recipeNames.add(entry.getKey());
                System.out.println(recipeNames);
                GUIController.receiveRecipeName(recipeNames);
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
        this.GUIController = GUIController;
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

