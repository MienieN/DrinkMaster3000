package src.client.controller;

import javafx.scene.control.Alert;
import src.client.boundary.AlcDrinkScreenManager;
import src.client.boundary.NonAlcDrinkScreenManager;
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
    private HashMap<String, HashSet<Ingredient>> recipes; // HashMap to store recipes and their ingredients
    private HashMap<String, HashSet<Ingredient>> validRecipes = new HashMap<>(); //All the recipes containing the chosen base drink.
    private HashMap<String, String> recipeInstructions = new HashMap<>();   // HashMap to store recipes and their instructions
    private AlcDrinkScreenManager alcDrinkScreenManager;                    // GUI controller for displaying alcoholic recipes
    private NonAlcDrinkScreenManager nonAlcDrinkScreenManager;              // GUI controller for displaying non-alcoholic recipes
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
        recipes = new HashMap<>();
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
    public void getRecipeInstructionsForChosenAlcRecipe() {
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
            if (resultSet.next()) {
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
                alert.setContentText("Recipe name: " + recipeName + "\nInstructions:\n" + instructions); //sets the content text of the alert
                alert.showAndWait(); //displays the alert and waits for the user to close it
            }
            //System.out.println(recipeInstructions);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getRecipeInstructionsForChosenNonAlcRecipe() {
        recipeInstructions.clear();
        String showRecipeSQL = "SELECT recipe_name, instructions FROM recipes WHERE recipe_name = ?";

        try (PreparedStatement statement = connection.prepareStatement(showRecipeSQL)) {
            statement.setString(1, nonAlcDrinkScreenManager.getSelectedRecipeNameForViewingRecipe());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String recipeName = resultSet.getString("recipe_name");
                String instructions = resultSet.getString("instructions");
                recipeInstructions.put(recipeName, instructions);

                //Pop up box
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Recipe");
                alert.setHeaderText(null);
                alert.setContentText("Recipe name: " + recipeName + "\nInstructions:\n" + instructions);
                alert.showAndWait();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if the chosen ingredients match any recipes in the database.
     * If a match is found, the recipe name is sent to the GUI controller.
     *
     * @param chosenIngredients The hash set of the chosen ingredients.
     */
    public void checkFullMatches(HashSet<Ingredient> chosenIngredients) {
        System.out.println("tetetet");
        Iterator<Map.Entry<String, HashSet<Ingredient>>> iterator = validRecipes.entrySet().iterator();
        System.out.println(chosenIngredients);
        while (iterator.hasNext()) {
            Map.Entry<String, HashSet<Ingredient>> entry = iterator.next();
            System.out.println(entry);
            //Find full match
            if (chosenIngredients.containsAll(entry.getValue())) {
                System.out.println("test");
                alcDrinkScreenManager.receiveBaseDrinkMatches(entry.getKey());
                iterator.remove();
            }
        }

        /*select distinct recipe_name,
count(distinct ingredient_name)
as ingredient_name
from recipes_ingredients
group by recipe_name
having recipe_name = ('Lennart') and count(ingredient_name in (chosenIngredients)) = count(distinct ingredient_name)*/
    }

    public void checkPartialMatchesIncludingBaseDrink(HashSet<Ingredient> chosenIngredients) {
        ArrayList<String> temp = new ArrayList<>();
        String sql = "SELECT recipe_name FROM recipes_ingredients WHERE ingredient_name IN (?";
        for (int i = 1; i < chosenIngredients.size(); i++) {
            sql += ", ?";
        }
        sql += ") GROUP BY recipe_name";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int index = 1;
            for (Ingredient ingredient : chosenIngredients) {
                statement.setString(index++, ingredient.getName());
            }
            //statement.setInt(index, chosenIngredients.size());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                temp.add(resultSet.getString("recipe_name"));
            }
            alcDrinkScreenManager.receivePartialMatches(temp);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkBaseDrinkOnly(String chosenBaseDrink) {
        Ingredient ingredient= ingredientsController.getIngredientFromArrayList(chosenBaseDrink);
        chosenIngredients.add(ingredient);
        String sql = "Select recipe_name from recipes_ingredients where ingredient_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, chosenBaseDrink);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                String recipeName = resultSet.getString("recipe_name");
                alcDrinkScreenManager.receiveBaseDrinkMatches(recipeName);
                //validRecipes.put(recipeName)
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getIngredientForMatches(String chosenIngredientName) {
        Ingredient ingredient = ingredientsController.getIngredientFromArrayList(chosenIngredientName);
        chosenIngredients.add(ingredient);
        checkPartialMatchesIncludingBaseDrink(chosenIngredients);
        checkFullMatches(chosenIngredients);
    }


    public void checkForNonAlcRecipe(String chosenIngredientName) {
        chosenIngredients.add(ingredientsController.getIngredientFromArrayList(chosenIngredientName));
        Iterator<Map.Entry<String, HashSet<Ingredient>>> iterator = recipes.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, HashSet<Ingredient>> entry = iterator.next();
            if (chosenIngredients.containsAll(entry.getValue())) {
                nonAlcDrinkScreenManager.receiveRecipeName(entry.getKey());
                iterator.remove();
            }
        }
    }

    /**
     * Sets the GUI controller for displaying recipes.
     *
     * @param GUIController The GUI controller for displaying recipes.
     */
    public void setAlcGUI(AlcDrinkScreenManager GUIController) {
        this.alcDrinkScreenManager = GUIController;
    }

    public void setNonAlcGUI(NonAlcDrinkScreenManager GUIController) {
        this.nonAlcDrinkScreenManager = GUIController;
    }

    public void resetChosenIngredients() {
        getRecipesFromDatabase();
        chosenIngredients.clear();
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