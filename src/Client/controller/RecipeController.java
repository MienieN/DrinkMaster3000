package src.Client.controller;

import src.Client.boundary.AlcDrinkScreenManager;
import src.Client.entity.Ingredient;

import java.sql.*;
import java.util.*;


/**
 * The RecipeController class manages the recipes and their interactions with the GUI.
 */
public class RecipeController {
    private HashMap<String, HashSet<Ingredient>> recipes = new HashMap<String, HashSet<Ingredient>>();   // HashMap to store recipes and their ingredients
    private AlcDrinkScreenManager GUIController;                                // GUI controller for displaying recipes
    private HashSet<Ingredient> chosenIngredients = new HashSet<>();        // List of chosen ingredients
    private Connection connection;                                              // Database connection
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
                            ingredientHashSet.add(new Ingredient(resultSet2.getString(j++), resultSet2.getBoolean(j) ));
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
     * Checks if a recipe can be made with the given chosen ingredient and notifies the GUI controller.
     * The method uses a {@link HashSet} due to performance of finding things based on name/key rather than index
     * The mothod uses an {@link Iterator} to be able to remove the found recipe while avoiding a {@link ConcurrentModificationException}
     * @param chosenIngredientName The name of the chosen ingredient.
     */

    public void checkForRecipe(String chosenIngredientName) {
        ArrayList<String> recipeNames = new ArrayList();
        chosenIngredients.add(ingredientsController.getIngredientFromArrayList(chosenIngredientName));
        Iterator<Map.Entry<String, HashSet<Ingredient>>> iterator = recipes.entrySet().iterator();
        while (iterator.hasNext()){
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
     * @param ingredientsController the {@link IngredientsController} created in {@ClientMain}
     */
    public void setIngredientsController(IngredientsController ingredientsController) {
        this.ingredientsController = ingredientsController;
    }
}

