package src.client.controller;

import javafx.scene.control.Alert;
import src.client.boundary.AlcDrinkScreenManager;
import src.client.boundary.DiscoverDrinkScreenManager;
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
    // Database connection
    private Connection connection;
    // GUI controller for displaying alcoholic recipes
    private AlcDrinkScreenManager alcDrinkScreenManager;
    // GUI controller for displaying non-alcoholic recipes
    private NonAlcDrinkScreenManager nonAlcDrinkScreenManager;
    // GUI controller for displaying discover recipes
    private DiscoverDrinkScreenManager discoverDrinkScreenManager;
    // HashMap to store recipes and their ingredients
    private HashMap<String, ArrayList<Ingredient>> recipes;
    // All the recipes containing the chosen base drink.
    private HashMap<String, ArrayList<Ingredient>> validRecipes = new HashMap<>();
    // HashMap to store recipes and their ingredients
    private HashMap<String, ArrayList<Ingredient>> discoverRecipes;
    // HashMap to store recipes and their instructions
    private HashMap<String, String> recipeInstructions = new HashMap<>();
    // List of partial matches
    private ArrayList<String> partialMatchList = new ArrayList<>();
    // List of full matches
    private ArrayList<String> fullMatches = new ArrayList<>();
    // List of matches without the base drink
    private ArrayList<String> matchesWithoutBaseDrink = new ArrayList<>();

    /**
     * Constructs a RecipeController object and initializes the database connection.
     */
    public RecipeController(Connection connection) {
        this.connection = connection;
        getRecipesFromDatabase();
        getRecipesFromDatabaseForDiscover();
    }

    /**
     * Retrieves recipes from the database and populates the recipes
     * HashMap with recipe names and their corresponding ingredients.
     */
    private void getRecipesFromDatabase() {
        recipes = new HashMap<>();
        String sql = "SELECT recipe_name FROM recipes";
        String sql2 = "SELECT recipes_ingredients.ingredient_name, ingredients.alcoholic, ingredients.frequency " +
                "FROM recipes_ingredients " +
                "JOIN ingredients ON recipes_ingredients.ingredient_name = ingredients.ingredient_name " +
                "WHERE recipe_name = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet recipeNames = statement.executeQuery();
            PreparedStatement statement2 = connection.prepareStatement(sql2);
            while (recipeNames.next()) {
                for (int i = 1; i <= recipeNames.getMetaData().getColumnCount(); i++) {
                    ArrayList<Ingredient> ingredientHashSet = new ArrayList<>();
                    String recipeName = recipeNames.getString(i);
                    statement2.setString(1, recipeName);
                    ResultSet resultSet2 = statement2.executeQuery();
                    while (resultSet2.next()) {
                        String ingredientName = resultSet2.getString("ingredient_name");
                        boolean isAlcoholic = resultSet2.getBoolean("alcoholic");
                        int frequency = resultSet2.getInt("frequency");
                        Ingredient ingredient = new Ingredient(ingredientName, isAlcoholic, frequency);
                        ingredientHashSet.add(ingredient);
                    }
                    recipes.put(recipeName, ingredientHashSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the recipes for the discover screen from the database and populates the discoverRecipes HashMap with
     * recipe names and their corresponding ingredients.
     */
    private void getRecipesFromDatabaseForDiscover() {
        discoverRecipes = new HashMap<>();
        String sql = "SELECT recipe_name FROM recipes WHERE speciality = TRUE";
        String sql2 = "SELECT recipes_ingredients.ingredient_name, ingredients.alcoholic, ingredients.frequency " +
                "FROM recipes_ingredients " +
                "JOIN ingredients ON recipes_ingredients.ingredient_name = ingredients.ingredient_name " +
                "WHERE recipe_name = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet recipeNames = statement.executeQuery();
            PreparedStatement statement2 = connection.prepareStatement(sql2);
            while (recipeNames.next()) {
                for (int i = 1; i <= recipeNames.getMetaData().getColumnCount(); i++) {
                    ArrayList<Ingredient> ingredientHashSet = new ArrayList<>();
                    String recipeName = recipeNames.getString(i);
                    statement2.setString(1, recipeName);
                    ResultSet resultSet2 = statement2.executeQuery();
                    while (resultSet2.next()) {
                        String ingredientName = resultSet2.getString("ingredient_name");
                        boolean isAlcoholic = resultSet2.getBoolean("alcoholic");
                        int frequency = resultSet2.getInt("frequency");
                        Ingredient ingredient = new Ingredient(ingredientName, isAlcoholic, frequency);
                        ingredientHashSet.add(ingredient);
                    }
                    discoverRecipes.put(recipeName, ingredientHashSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the recipe instructions for the chosen recipe and displays them in an alert pop-up box.
     */
    public void getRecipeInstructionsForChosenAlcRecipe() {
        recipeInstructions.clear();
        // Declares an SQL query string that selects the recipe_name and instructions column from the table named recipes
        // Filtering by recipe_name. The ? is a placeholder for a parameter that will be filled in later.
        String showRecipeSQL = "SELECT recipe_name, instructions FROM recipes WHERE recipe_name = ?";

        // This line begins a try-with-resources block. It prepares a PreparedStatement object using the SQL query
        // String showRecipeSQL. The connection object is assumed to be a database connection.
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

                // Pop up box
                //Creates an alert object
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                // Sets the title of the alert
                alert.setTitle("Recipe");
                // Sets the header text of the alert
                alert.setHeaderText(null);
                // Sets the content text of the alert
                alert.setContentText("Recipe name: " + recipeName + "\nInstructions:\n" + instructions);
                // Displays the alert and waits for the user to close it
                alert.showAndWait();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the recipe instructions for the chosen non-alcoholic recipe and displays them in an alert pop-up box.
     */
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

                // Pop up box
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
     * Gets the recipe instructions for the chosen speciality recipe and displays them in an alert pop-up box.
     */
    public void getRecipeInstructionsForChosenDiscoverRecipe() {
        recipeInstructions.clear();
        String showRecipeSQL = "SELECT recipe_name, instructions FROM recipes WHERE recipe_name = ?";

        try (PreparedStatement statement = connection.prepareStatement(showRecipeSQL)) {
            statement.setString(1, discoverDrinkScreenManager.getSelectedRecipeNameForViewingRecipe());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String recipeName = resultSet.getString("recipe_name");
                String instructions = resultSet.getString("instructions");
                recipeInstructions.put(recipeName, instructions);

                // Pop up box
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
     * Gets the recipe names from the database and sends them to the GUI controller.
     *
     * @param chosenBaseDrink The chosen base drink.
     */
    public void getBaseDrinkCompatibleRecipesFromDatabase(String chosenBaseDrink) {
        // A new ArrayList to store the ingredients for a recipe from the database
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        String getRecipeIngredients = "SELECT * FROM " +
                "ingredients WHERE ingredient_name IN (SELECT ingredient_name FROM " +
                "recipes_ingredients WHERE recipe_name = ?)";

        String getRecipeNames = "SELECT recipe_name FROM recipes_ingredients WHERE ingredient_name = ?";

        try (PreparedStatement statementGetNames = connection.prepareStatement(getRecipeNames)) {
            PreparedStatement statmentGetIngredients = connection.prepareStatement(getRecipeIngredients);
            statementGetNames.setString(1, chosenBaseDrink);
            ResultSet resultSet = statementGetNames.executeQuery();
            while (resultSet.next()) {
                String recipeName = resultSet.getString("recipe_name");
                // alcDrinkScreenManager.receiveBaseDrinkMatches(recipeName);
                statmentGetIngredients.setString(1, recipeName);
                ResultSet resultSetIngredients = statmentGetIngredients.executeQuery();
                while (resultSetIngredients.next()) {
                    String ingredientName = resultSetIngredients.getString("ingredient_name");
                    boolean isAlcoholic = resultSetIngredients.getBoolean("alcoholic");
                    int frequency = resultSetIngredients.getInt("frequency");
                    Ingredient ingredientToAdd = new Ingredient(ingredientName, isAlcoholic, frequency);
                    ingredients.add(ingredientToAdd);
                }
                validRecipes.put(recipeName, ingredients);
                ingredients = new ArrayList<>();
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
    public void checkFullAlcMatches(ArrayList<Ingredient> chosenIngredients) {
        fullMatches = new ArrayList<>();
        Iterator<Map.Entry<String, ArrayList<Ingredient>>> validRecipeIterator = validRecipes.entrySet().iterator();
        Iterator<Map.Entry<String, ArrayList<Ingredient>>> remainingRecipesIterator = recipes.entrySet().iterator();
        while (validRecipeIterator.hasNext()) {
            Map.Entry<String, ArrayList<Ingredient>> entry = validRecipeIterator.next();
            if (chosenIngredients.containsAll(entry.getValue())) {
                if (fullMatches.isEmpty()) {
                    fullMatches.add("Full Matches:");
                }
                fullMatches.add(entry.getKey());
                partialMatchList.remove(entry.getKey());
            }
        }
        while (remainingRecipesIterator.hasNext()) {
            Map.Entry<String, ArrayList<Ingredient>> entry = remainingRecipesIterator.next();
            if (chosenIngredients.containsAll(entry.getValue())) {
                String recipe_name = entry.getKey();

                if (!fullMatches.contains(recipe_name)) {
                    if (matchesWithoutBaseDrink.isEmpty()) {
                        matchesWithoutBaseDrink.add("Matches without base drink:");
                    }
                    matchesWithoutBaseDrink.add(recipe_name);
                }
                remainingRecipesIterator.remove();
            }
        }
    }

    /**
     * Checks if the chosen ingredients match any non-alcoholic recipes in the database.
     * If a match is found, the recipe name is sent to the GUI controller.
     *
     * @param chosenIngredients The hash set of the chosen ingredients.
     */
    public void checkFullNonAlcMatches(ArrayList<Ingredient> chosenIngredients) {
        Iterator<Map.Entry<String, ArrayList<Ingredient>>> RecipeIterator = recipes.entrySet().iterator();
        while (RecipeIterator.hasNext()) {
            Map.Entry<String, ArrayList<Ingredient>> entry = RecipeIterator.next();
            // Find full match
            if (chosenIngredients.containsAll(entry.getValue())) {
                if (fullMatches.isEmpty()) {
                    fullMatches.add("Full Matches:");
                }
                fullMatches.add(entry.getKey());
                partialMatchList.remove(entry.getKey());
                RecipeIterator.remove();
            }
        }
    }

    /**
     * Checks if the chosen ingredients fully match any discover recipes in the database.
     * If a match is found, the recipe name is sent to the GUI controller.
     *
     * @param chosenIngredients The hash set of the chosen ingredients.
     */
    public void checkFullDiscoverMatches(ArrayList<Ingredient> chosenIngredients) {
        Iterator<Map.Entry<String, ArrayList<Ingredient>>> RecipeIterator = discoverRecipes.entrySet().iterator();
        while (RecipeIterator.hasNext()) {
            Map.Entry<String, ArrayList<Ingredient>> entry = RecipeIterator.next();
            // Find full match
            if (chosenIngredients.containsAll(entry.getValue())) {
                if (fullMatches.isEmpty()) {
                    fullMatches.add("Full Matches:");
                }
                fullMatches.add(entry.getKey());
                partialMatchList.remove(entry.getKey());
                RecipeIterator.remove();
            }
        }
    }

    /**
     * Responsible for getting the partial matches based on the alcoholic base drink chosen
     *
     * @param chosenIngredients the list of chosen ingredients in case of alcoholic
     *                          drinks contains the chosen base drink aswell
     */
    public void checkPartialMatchesIncludingBaseDrink(ArrayList<Ingredient> chosenIngredients) {
        String sql = "SELECT recipe_name, count (DISTINCT ingredient_name) FROM (SELECT * FROM recipes_ingredients " +
                "WHERE recipe_name IN ((SELECT recipe_name FROM recipes_ingredients " +
                "WHERE ingredient_name = ?))) AS recipes " +
                "WHERE ingredient_name IN (?";
        for (int i = 1; i < chosenIngredients.size(); i++) {
            sql += ", ?";
        }
        sql += ") GROUP BY recipe_name ORDER BY count DESC";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int index = 2;
            statement.setString(1, chosenIngredients.get(0).getName());
            for (int i = 0; i < chosenIngredients.size(); i++) {
                statement.setString(index++, chosenIngredients.get(i).getName());
            }
            ResultSet resultSet = statement.executeQuery();
            partialMatchList.add("Partial Matches:");
            while (resultSet.next()) {
                String recipe_name = resultSet.getString("recipe_name");
                if (!fullMatches.contains(recipe_name)) {
                    partialMatchList.add(recipe_name);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks for partial matches of non-alcoholic drinks based on the chosen ingredients.
     *
     * @param chosenIngredients The list of chosen ingredients.
     */
    public void checkPartialMatchesOfNonAlcDrinks(ArrayList<Ingredient> chosenIngredients) {
        String sql = "SELECT recipe_name, count (DISTINCT ingredient_name) FROM (SELECT * FROM recipes_ingredients " +
                "WHERE recipe_name NOT IN ((SELECT recipe_name " +
                "FROM recipes_ingredients INNER JOIN " +
                "ingredients ON recipes_ingredients.ingredient_name = ingredients.ingredient_name " +
                "WHERE alcoholic = TRUE)))AS recipes " +
                "WHERE ingredient_name IN (?";
        for (int i = 1; i < chosenIngredients.size(); i++) {
            sql += ", ?";
        }
        sql += ") GROUP BY recipe_name ORDER BY count DESC";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int index = 1;
            // statement.setString(2, chosenIngredients.get(0).getName());
            for (int i = 0; i < chosenIngredients.size(); i++) {
                statement.setString(index++, chosenIngredients.get(i).getName());
            }
            // statement.setInt(index, chosenIngredients.size());
            ResultSet resultSet = statement.executeQuery();
            partialMatchList.add("Partial Matches:");
            while (resultSet.next()) {
                String recipe_name = resultSet.getString("recipe_name");
                if (!fullMatches.contains(recipe_name)) {
                    partialMatchList.add(recipe_name);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks for partial matches of discover drinks based on the chosen ingredients.
     *
     * @param chosenIngredients The list of chosen ingredients.
     */
    public void checkPartialMatchesOfDiscoverDrinks(ArrayList<Ingredient> chosenIngredients) {
        String sql = "SELECT recipe_name, count (DISTINCT ingredient_name) FROM (SELECT * FROM recipes_ingredients " +
                "WHERE recipe_name NOT IN ((SELECT recipe_name " +
                "FROM recipes_ingredients INNER JOIN " +
                "ingredients ON recipes_ingredients.ingredient_name = ingredients.ingredient_name " +
                "WHERE recipe_name IN (SELECT recipe_name FROM recipes WHERE speciality = FALSE)))) AS recipes " +
                "WHERE ingredient_name IN (?";
        for (int i = 1; i < chosenIngredients.size(); i++) {
            sql += ", ?";
        }
        sql += ") GROUP BY recipe_name ORDER BY count DESC";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int index = 1;
            // statement.setString(2, chosenIngredients.get(0).getName());
            for (int i = 0; i < chosenIngredients.size(); i++) {
                statement.setString(index++, chosenIngredients.get(i).getName());
            }
            // statement.setInt(index, chosenIngredients.size());
            ResultSet resultSet = statement.executeQuery();
            partialMatchList.add("Partial Matches:");
            while (resultSet.next()) {
                String recipe_name = resultSet.getString("recipe_name");
                if (!fullMatches.contains(recipe_name)) {
                    partialMatchList.add(recipe_name);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends the matches to the GUI controller.
     *
     * @param screen The screen to send the matches to.
     */
    public void sendRecipeMatches(String screen) {
        ArrayList<String> matches = new ArrayList<>();

        if (!fullMatches.isEmpty()) {
            matches.addAll(fullMatches);
            matches.add("");
        }

        if (partialMatchList.size() > 1) {
            matches.addAll(partialMatchList);
            matches.add("");
        }

        if (!matchesWithoutBaseDrink.isEmpty()) {
            matches.addAll(matchesWithoutBaseDrink);
        }

        if(screen.equals("alc")) {
            alcDrinkScreenManager.receiveMatches(matches);
        } else if (screen.equals("non-alc")) {
            nonAlcDrinkScreenManager.receiveMatches(matches);
        } else if (screen.equals("other")) {
            discoverDrinkScreenManager.receiveMatches(matches);
        }
    }

    /**
     * Checks for alcoholic recipe matches based on the chosen ingredients.
     *
     * @param chosenIngredients The list of chosen ingredients.
     */
    public void checkForAlcRecipe(ArrayList<Ingredient> chosenIngredients) {
        checkFullAlcMatches(chosenIngredients);
        checkPartialMatchesIncludingBaseDrink(chosenIngredients);
        sendRecipeMatches("alc");
    }

    /**
     * Checks for non-alcoholic recipe matches based on the chosen ingredients.
     *
     * @param chosenIngredients The list of chosen ingredients.
     */
    public void checkForNonAlcRecipe(ArrayList<Ingredient> chosenIngredients) {
        checkFullNonAlcMatches(chosenIngredients);
        checkPartialMatchesOfNonAlcDrinks(chosenIngredients);
        sendRecipeMatches("non-alc");
    }

    /**
     * Checks for discover recipe matches based on the chosen ingredients.
     *
     * @param chosenIngredients The list of chosen ingredients.
     */
    public void checkForDiscoverRecipe(ArrayList<Ingredient> chosenIngredients) {
        checkFullDiscoverMatches(chosenIngredients);
        checkPartialMatchesOfDiscoverDrinks(chosenIngredients);
        sendRecipeMatches("other");
    }

    /**
     * Sets the GUI controller for displaying recipes.
     *
     * @param AlcGUIController The GUI controller for displaying recipes.
     */
    public void setAlcGUI(AlcDrinkScreenManager AlcGUIController) {
        this.alcDrinkScreenManager = AlcGUIController;
    }

    /**
     * Sets the GUI controller for displaying non-alcoholic recipes.
     *
     * @param NonAlcGUIController The GUI controller for displaying non-alcoholic recipes.
     */
    public void setNonAlcGUI(NonAlcDrinkScreenManager NonAlcGUIController) {
        this.nonAlcDrinkScreenManager = NonAlcGUIController;
    }

    /**
     * Sets the GUI controller for displaying discover recipes.
     *
     * @param discoverGUIController The GUI controller for displaying discover recipes.
     */
    public void setDiscoverGUI(DiscoverDrinkScreenManager discoverGUIController) {
        this.discoverDrinkScreenManager = discoverGUIController;
    }

    /**
     * Resets the recipes.
     */
    public void resetRecipes() {
        matchesWithoutBaseDrink.clear();
        fullMatches.clear();
        partialMatchList.clear();
    }

}