package src.client.controller;

import src.client.boundary.AlcDrinkScreenManager;
import src.client.boundary.DiscoverDrinkScreenManager;
import src.client.boundary.NonAlcDrinkScreenManager;
import src.client.entity.Ingredient;
import java.sql.*;
import java.util.ArrayList;

/**
 * The IngredientsController class manages interactions with ingredients in the database.
 */
public class IngredientsController {
    private RecipeController recipeController;
    private ArrayList<Ingredient> ingredients;          // The list of ingredients
    private ArrayList<Ingredient> relevantIngredients;  //the list of ingredients sorted by
                                                        // compatability with your base drink
    private ArrayList<Ingredient> chosenIngredients;
    private Connection connection;                      // The database connection
    private AlcDrinkScreenManager alcDrinkScreenManager;
    private NonAlcDrinkScreenManager nonAlcDrinkScreenManager;
    private DiscoverDrinkScreenManager discoverDrinkScreenManager;

    /**
     * Constructs an IngredientsController object and initializes the database connection.
     */
    public IngredientsController(Connection connection) {
        this.connection = connection;
        // Retrieve all ingredients from the database
        getAllIngredientsFromDatabase();
        chosenIngredients = new ArrayList<>();
    }

    public void setRecipeController(RecipeController recipeController) {
        this.recipeController = recipeController;
    }

    /**
     * Receives all ingredients from the database and puts them in an arraylist of ingredients objects
     *
     * @return An arraylist of ingredients
     */
    //TODO either get 2 lists or sort the list based on alc content
    public void getAllIngredientsFromDatabase() {
        ingredients = new ArrayList<>();
        String allIngredients = "select * from ingredients order by frequency desc"; //query depending on how the alcohol marker is set

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

    public void getIngredientsBasedOnBaseDrink(String baseDrinkName) {
        relevantIngredients = new ArrayList<>();
        String relevantIngredientsSQL = "with recipe_ingredients as " +
                "(select * from ingredients where ingredient_name in" +
                "(select ingredient_name from recipes_ingredients where recipe_name in" +
                "(select recipe_name from recipes_ingredients where " +
                "ingredient_name in (?)) and ingredient_name != ?) " +
                "order by frequency desc), " +
                "remaining_ingredients as " +
                "(select * from ingredients where ingredient_name not in " +
                "(select ingredient_name from recipes_ingredients where recipe_name in " +
                "(select recipe_name from recipes_ingredients where ingredient_name in (?))) " +
                "order by frequency desc) " +
                "select * from recipe_ingredients union all select * from remaining_ingredients";

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

    public ArrayList<String> getAlcoholicIngredientNames() {
        ArrayList<String> ingredientNames = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getAlcoholic()) {
                ingredientNames.add(ingredient.getName());
            }
        }
        return ingredientNames;
    }

    public ArrayList<String> getNonAlcoholicIngredientNames() {
        ArrayList<String> ingredientNames = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            if (!ingredient.getAlcoholic()) {
                ingredientNames.add(ingredient.getName());
            }
        }
        return ingredientNames;
    }

    public void chooseIngredient(String ingredientName, String screen) {
        Ingredient ingredient = getIngredientFromArrayList(ingredientName);
        chosenIngredients.add(ingredient);
        switch (screen){
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

    public void undoIngredientChoice(String screen) {
        Ingredient ingredient = chosenIngredients.get(chosenIngredients.size()-1);
        switch (screen){
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
    public void updateChosenIngredientsList(String screen){
        ArrayList<String> chosenIngredientNames = new ArrayList<>();
        for (Ingredient ingredient : chosenIngredients){
            chosenIngredientNames.add(ingredient.getName());
        }
        switch (screen){
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

    public void setAlcGUI(AlcDrinkScreenManager alcDrinkScreenManager) {
        this.alcDrinkScreenManager = alcDrinkScreenManager;
    }

    public void removeChoice(String name, String screen) {
        Ingredient ingredient = getIngredientFromArrayList(name);
        switch (screen){
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

    public void resetChosenIngredients() {
        chosenIngredients.clear();

    }

    public void setNonAlcGUI(NonAlcDrinkScreenManager nonAlcDrinkScreenManager) {
        this.nonAlcDrinkScreenManager = nonAlcDrinkScreenManager;
    }

    public void setDiscoverGUI(DiscoverDrinkScreenManager discoverGUIController) {
        this.discoverDrinkScreenManager = discoverGUIController;
    }
}