package src.Client.controller;

import src.Client.boundary.AlcDrinkScreenManager;
import src.Client.entity.Ingredient;

import java.sql.*;
import java.util.*;


/**
 * The RecipeController class manages the recipes and their interactions with the GUI.
 */
public class RecipeController {
    private HashMap<String, HashSet<Ingredient>> recipes = new HashMap<>();   // HashMap to store recipes and their ingredients
    private AlcDrinkScreenManager GUIController;                                // GUI controller for displaying recipes
    private HashSet<Ingredient> chosenIngredients = new HashSet<>();        // List of chosen ingredients
    private Connection connection;                                              // Database connection
    private IngredientsController ingredientsController;

    /**
     * Constructs a RecipeController object and initializes the database connection.
     */
    public RecipeController() {
        connect();
        getRecipesFromDatabase();
    }

    /**
     * Establishes a connection to the database.
     */
    public void connect() {
        try {
            // Establish connection to the PostgreSQL database
            connection = DriverManager.getConnection("jdbc:postgresql://pgserver.mau.se:5432/drinkmaster3000", "ao7503", "t360bxdp");
            System.out.println("Connection established");
        } catch (SQLException e) {
            System.out.println("Error in connection");
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves recipes from the database and populates the recipes HashMap with recipe names and their corresponding ingredients.
     */
    private void getRecipesFromDatabase() {
        String sql = "Select recipe_name from recipes";
        String sql2 = "Select ingredient_name from recipes_ingredients where recipe_name = ?";
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
                            ingredientHashSet.add(new Ingredient(resultSet2.getString(j)));
                        }
                    }
                    recipes.put(recipeName, ingredientHashSet);
                }
            }
            System.out.println(recipes);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if a recipe can be made with the given chosen ingredient and notifies the GUI controller.
     *
     * @param chosenIngredientName The name of the chosen ingredient.
     */
    //TODO: this method should show all the drink recipes, it should not show duplicates, and
    // the method should not add drinks only containing the latest ingredient chosen, it should update
    // the drinks shown based on the accumulated ingredients thus far and "refine" the search.
    /*public void checkForRecipe(String chosenIngredientName) {
        ArrayList<String> recipeNames = new ArrayList();
        chosenIngredients.add(ingredientsController.getIngredientFromArrayList(chosenIngredientName));
        for (String recipeName : recipes.keySet()) {
            if (chosenIngredients.containsAll(recipes.get(recipeName))) {
                System.out.println("test");
                recipeNames.add(recipeName);
                GUIController.receiveRecipeName(recipeNames);
                recipes.remove(recipeName);
            }
        }
    }*/
    public void checkForRecipe(String chosenIngredientName) {
        ArrayList<String> recipeNames = new ArrayList();
        chosenIngredients.add(ingredientsController.getIngredientFromArrayList(chosenIngredientName));
        Iterator<Map.Entry<String, HashSet<Ingredient>>> iterator = recipes.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, HashSet<Ingredient>> entry = iterator.next();
            System.out.println(chosenIngredients.toString());
            //System.out.println(entry.getValue().toString());
            if (chosenIngredients.containsAll(entry.getValue())) {
                System.out.println("test");
                recipeNames.add(entry.getKey());
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
     * @param ingredientsController the {@link IngredientsController} created in {@link src.Client.ClientMain}
     */
    public void setIngredientsController(src.Client.controller.IngredientsController ingredientsController) {
        this.ingredientsController = ingredientsController;
    }
}

