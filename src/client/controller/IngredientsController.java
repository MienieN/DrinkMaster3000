package src.client.controller;

import src.client.boundary.AlcDrinkScreenManager;
import src.client.boundary.DiscoverDrinkScreenManager;
import src.client.boundary.NonAlcDrinkScreenManager;
import src.client.entity.Ingredient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The IngredientsController class manages interactions with ingredients in the database.
 */
public class IngredientsController {
    // The RecipeController object
    private RecipeController recipeController;

    // The database connection
    private Connection connection;

    // The AlcDrinkScreenManager object
    private AlcDrinkScreenManager alcDrinkScreenManager;
    // The NonAlcDrinkScreenManager object
    private NonAlcDrinkScreenManager nonAlcDrinkScreenManager;
    // The DiscoverDrinkScreenManager object
    private DiscoverDrinkScreenManager discoverDrinkScreenManager;

    // The list of ingredients
    private ArrayList<Ingredient> ingredients;
    // The list of ingredients sorted by compatability with your base drink
    private ArrayList<Ingredient> relevantIngredients;
    // The list of chosen ingredients
    private ArrayList<Ingredient> chosenIngredients;

    /**
     * Constructs an IngredientsController object and initializes the database connection.
     */
    public IngredientsController(Connection connection) {
        this.connection = connection;
        // Retrieve all ingredients from the database
        getAllIngredientsFromDatabase();
        chosenIngredients = new ArrayList<>();
    }

    /**
     * Sets the RecipeController object.
     *
     * @param recipeController The RecipeController object
     */
    public void setRecipeController(RecipeController recipeController) {
        this.recipeController = recipeController;
    }

    /**
     * Receives all ingredients from the database and puts them in an arraylist of ingredients objects
     *
     * @return An arraylist of ingredients
     */
    // TODO either get 2 lists or sort the list based on alc content
    public void getAllIngredientsFromDatabase() {
        ingredients = new ArrayList<>();
        String allIngredients = "SELECT * FROM ingredients ORDER BY frequency DESC ";

        try (PreparedStatement statement = connection.prepareStatement(allIngredients)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String ingredientName = resultSet.getString("ingredient_name");
                boolean isAlcoholic = resultSet.getBoolean("alcoholic");
                int frequency = resultSet.getInt("frequency");
                Ingredient ingredient = new Ingredient(ingredientName, isAlcoholic, frequency);
                ingredients.add(ingredient);
            }

        } catch (SQLException e) {
            System.out.println("Error in getting ingredients from database");
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the ingredients that are compatible with the base drink.
     *
     * @param baseDrinkName The name of the base drink
     */
    public void getIngredientsBasedOnBaseDrink(String baseDrinkName) {
        relevantIngredients = new ArrayList<>();
        String relevantIngredientsSQL = "WITH recipe_ingredients AS " +
                "(SELECT * FROM ingredients WHERE ingredient_name IN" +
                "(SELECT ingredient_name FROM recipes_ingredients WHERE recipe_name IN " +
                "(SELECT recipe_name FROM recipes_ingredients WHERE " +
                "ingredient_name IN (?)) AND ingredient_name != ?) " +
                "ORDER BY frequency DESC), " +
                "remaining_ingredients AS " +
                "(SELECT * FROM ingredients WHERE ingredient_name NOT IN " +
                "(SELECT ingredient_name FROM recipes_ingredients WHERE recipe_name IN " +
                "(SELECT recipe_name FROM recipes_ingredients WHERE ingredient_name IN (?))) " +
                "ORDER BY frequency DESC) " +
                "SELECT * FROM recipe_ingredients UNION ALL SELECT * FROM remaining_ingredients";

        try (PreparedStatement statement = connection.prepareStatement(relevantIngredientsSQL)) {
            statement.setString(1, baseDrinkName);
            statement.setString(2, baseDrinkName);
            statement.setString(3, baseDrinkName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String ingredientName = resultSet.getString("ingredient_name");
                boolean isAlcoholic = resultSet.getBoolean("alcoholic");
                int frequency = resultSet.getInt("frequency");
                Ingredient ingredient = new Ingredient(ingredientName, isAlcoholic, frequency);
                relevantIngredients.add(ingredient);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves an ingredient object from the ArrayList based on the name of the ingredient.
     *
     * @param ingredientName The name of the ingredient.
     * @return The Ingredient object corresponding to the given name, or null if not found.
     */
    public Ingredient getIngredientFromArrayList(String ingredientName) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getName().equals(ingredientName)) {
                return ingredient;
            }
        }
        return null;
    }

    /**
     * Retrieves a list of ingredient names.
     *
     * @return An ArrayList containing the names of all ingredients.
     */
    public ArrayList<String> getIngredientNames() {
        ArrayList<String> ingredientNames = new ArrayList<>();
        for (Ingredient ingredient : relevantIngredients) {
            ingredientNames.add(ingredient.getName());
        }
        return ingredientNames;
    }

    /**
     * Gets the names of all alcoholic ingredients.
     *
     * @return An ArrayList containing the names of all alcoholic ingredients.
     */
    public ArrayList<String> getAlcoholicIngredientNames() {
        ArrayList<String> ingredientNames = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getAlcoholic()) {
                ingredientNames.add(ingredient.getName());
            }
        }
        return ingredientNames;
    }

    /**
     * Gets the names of all non-alcoholic ingredients.
     *
     * @return An ArrayList containing the names of all non-alcoholic ingredients.
     */
    public ArrayList<String> getNonAlcoholicIngredientNames() {
        ArrayList<String> ingredientNames = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            if (!ingredient.getAlcoholic()) {
                ingredientNames.add(ingredient.getName());
            }
        }
        return ingredientNames;
    }

    /**
     * Chooses an ingredient and adds it to the list of chosen ingredients.
     * Checks for recipes based on the chosen ingredients.
     *
     * @param ingredientName The name of the ingredient
     * @param screen         The screen where the ingredient was chosen
     */
    public void chooseIngredient(String ingredientName, String screen) {
        Ingredient ingredient = getIngredientFromArrayList(ingredientName);
        chosenIngredients.add(ingredient);
        switch (screen) {
            case "alc":
                recipeController.checkForAlcRecipe(chosenIngredients);
                break;
            case "non-alc":
                recipeController.checkForNonAlcRecipe(chosenIngredients);
                break;
            case "other":
                recipeController.checkForDiscoverRecipe(chosenIngredients);
                break;
        }
        updateChosenIngredientsList(screen);
    }

    /**
     * Undoes the last ingredient choice.
     *
     * @param screen The screen where the ingredient was chosen
     */
    public void undoIngredientChoice(String screen) {
        Ingredient ingredient = chosenIngredients.get(chosenIngredients.size() - 1);
        switch (screen) {
            case "alc":
                alcDrinkScreenManager.addBackIngredient(ingredient.getName());
                chosenIngredients.remove(ingredient);
                recipeController.checkForAlcRecipe(chosenIngredients);
                break;
            case "non-alc":
                nonAlcDrinkScreenManager.addBackIngredient(ingredient.getName());
                chosenIngredients.remove(ingredient);
                recipeController.checkForNonAlcRecipe(chosenIngredients);
                break;
            case "other":
                discoverDrinkScreenManager.addBackIngredient(ingredient.getName());
                chosenIngredients.remove(ingredient);
                recipeController.checkForDiscoverRecipe(chosenIngredients);
                break;
        }
        chosenIngredients.remove(ingredient);
        updateChosenIngredientsList(screen);
    }

    /**
     * Updates the list of chosen ingredients.
     *
     * @param screen The screen where the ingredient was chosen
     */
    public void updateChosenIngredientsList(String screen) {
        ArrayList<String> chosenIngredientNames = new ArrayList<>();
        for (Ingredient ingredient : chosenIngredients) {
            chosenIngredientNames.add(ingredient.getName());
        }
        switch (screen) {
            case "alc":
                alcDrinkScreenManager.receiveChosenIngredients(chosenIngredientNames);
                break;
            case "non-alc":
                nonAlcDrinkScreenManager.receiveChosenIngredients(chosenIngredientNames);
                break;
            case "other":
                discoverDrinkScreenManager.receiveChosenIngredients(chosenIngredientNames);
                break;
        }
    }

    /**
     * Sets the AlcDrinkScreenManager object.
     *
     * @param alcDrinkScreenManager The AlcDrinkScreenManager object
     */
    public void setAlcGUI(AlcDrinkScreenManager alcDrinkScreenManager) {
        this.alcDrinkScreenManager = alcDrinkScreenManager;
    }

    /**
     * Removes a choice from the list of chosen ingredients.
     *
     * @param name   The name of the ingredient
     * @param screen The screen where the ingredient was chosen
     */
    public void removeChoice(String name, String screen) {
        Ingredient ingredient = getIngredientFromArrayList(name);
        switch (screen) {
            case "alc":
                alcDrinkScreenManager.addBackIngredient(name);
                break;
            case "non-alc":
                nonAlcDrinkScreenManager.addBackIngredient(name);
                break;
            case "other":
                discoverDrinkScreenManager.addBackIngredient(name);
                break;
        }
        chosenIngredients.remove(ingredient);
        updateChosenIngredientsList(screen);
    }

    /**
     * Resets the list of chosen ingredients.
     */
    public void resetChosenIngredients() {
        chosenIngredients.clear();
    }

    /**
     * Sets the NonAlcDrinkScreenManager object.
     *
     * @param nonAlcDrinkScreenManager The NonAlcDrinkScreenManager object
     */
    public void setNonAlcGUI(NonAlcDrinkScreenManager nonAlcDrinkScreenManager) {
        this.nonAlcDrinkScreenManager = nonAlcDrinkScreenManager;
    }

    /**
     * Sets the DiscoverDrinkScreenManager object.
     *
     * @param discoverGUIController The DiscoverDrinkScreenManager object
     */
    public void setDiscoverGUI(DiscoverDrinkScreenManager discoverGUIController) {
        this.discoverDrinkScreenManager = discoverGUIController;
    }
}