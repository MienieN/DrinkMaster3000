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
    private HashMap<String, ArrayList<Ingredient>> recipes; // HashMap to store recipes and their ingredients
    private HashMap<String, ArrayList<Ingredient>> validRecipes = new HashMap<>(); //All the recipes containing the chosen base drink.
    private HashMap<String, String> recipeInstructions = new HashMap<>();   // HashMap to store recipes and their instructions
    private AlcDrinkScreenManager alcDrinkScreenManager;                    // GUI controller for displaying alcoholic recipes
    private NonAlcDrinkScreenManager nonAlcDrinkScreenManager;              // GUI controller for displaying non-alcoholic recipes
    private ArrayList<Ingredient> chosenIngredients = new ArrayList<>();        // List of chosen ingredients
    private ArrayList<String> partialMatchList;
    private ArrayList<String> fullMatches = new ArrayList<>();
    private ArrayList<String> matchesWithoutBaseDrink = new ArrayList<>();
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
        String sql2 = "select recipes_ingredients.ingredient_name, ingredients.alcoholic, ingredients.frequency from recipes_ingredients " +
                "join ingredients ON recipes_ingredients.ingredient_name = ingredients.ingredient_name " +
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
    public void checkFullAlcMatches(ArrayList<Ingredient> chosenIngredients) {
        Iterator<Map.Entry<String, ArrayList<Ingredient>>> validRecipeIterator = validRecipes.entrySet().iterator();
        Iterator<Map.Entry<String, ArrayList<Ingredient>>> remainingRecipesIterator = recipes.entrySet().iterator();
        while (validRecipeIterator.hasNext()) {
            Map.Entry<String, ArrayList<Ingredient>> entry = validRecipeIterator.next();
            //Find full match
            if (chosenIngredients.containsAll(entry.getValue())) {
                if (fullMatches.isEmpty()) {
                    fullMatches.add("Full Matches:");
                }
                fullMatches.add(entry.getKey());
                partialMatchList.remove(entry.getKey());
                validRecipeIterator.remove();
            }
        }
        while (remainingRecipesIterator.hasNext()) {
            Map.Entry<String, ArrayList<Ingredient>> entry = remainingRecipesIterator.next();
            //Find full match
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

    public void checkFullNonAlcMatches(ArrayList<Ingredient> chosenIngredients) {
        Iterator<Map.Entry<String, ArrayList<Ingredient>>> RecipeIterator = recipes.entrySet().iterator();
        while (RecipeIterator.hasNext()) {
            Map.Entry<String, ArrayList<Ingredient>> entry = RecipeIterator.next();
            //Find full match
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
     * @param chosenIngredients the list of chosen ingredients in case of alcoholic drinks contains the chosen base drink aswell
     */
    public void checkPartialMatchesIncludingBaseDrink(ArrayList<Ingredient> chosenIngredients) {
        partialMatchList = new ArrayList<>();
        String sql = "SELECT recipe_name, count (distinct ingredient_name) FROM (select * from recipes_ingredients " +
                "where recipe_name in ((select recipe_name from recipes_ingredients " +
                "where ingredient_name = ?)))as recipes " +
                "WHERE ingredient_name in (?";
        for (int i = 1; i < chosenIngredients.size(); i++) {
            sql += ", ?";
        }
        sql += ") group by recipe_name order by count desc";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int index = 2;
            statement.setString(1, chosenIngredients.get(0).getName());
            //statement.setString(2, chosenIngredients.get(0).getName());
            for (int i = 0; i < chosenIngredients.size(); i++) {
                statement.setString(index++, chosenIngredients.get(i).getName());
            }
            //statement.setInt(index, chosenIngredients.size());
            ResultSet resultSet = statement.executeQuery();
            partialMatchList.add("Partial Matches:");
            while (resultSet.next()) {
                String recipe_name = resultSet.getString("recipe_name");
                if (!fullMatches.contains(recipe_name)) {
                    partialMatchList.add(recipe_name);
                }

            }
            //alcDrinkScreenManager.receivePartialMatches(partialMatchList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkPartialMatchesOfDrinks(ArrayList<Ingredient> chosenIngredients) {
        partialMatchList = new ArrayList<>();
        String sql = "SELECT recipe_name, count (distinct ingredient_name) FROM (select * from recipes_ingredients " +
                "where recipe_name not in ((select recipe_name " +
                "from recipes_ingredients inner join " +
                "ingredients on recipes_ingredients.ingredient_name = ingredients.ingredient_name where alcoholic = true)))as recipes " +
                "WHERE ingredient_name in (?";
        for (int i = 1; i < chosenIngredients.size(); i++) {
            sql += ", ?";
        }
        sql += ") group by recipe_name order by count desc";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int index = 1;
            //statement.setString(2, chosenIngredients.get(0).getName());
            for (int i = 0; i < chosenIngredients.size(); i++) {
                statement.setString(index++, chosenIngredients.get(i).getName());
            }
            //statement.setInt(index, chosenIngredients.size());
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

    public void sendMatches(String screen) {
        ArrayList<String> matches = new ArrayList<>();

        if (!fullMatches.isEmpty()) {
            matches.addAll(fullMatches);
            matches.add("");
        }
        if (partialMatchList.size() > 1) {
            matches.addAll(partialMatchList);
            matches.add("");
        }

        System.out.println(partialMatchList);
        if (!matchesWithoutBaseDrink.isEmpty()) {

            matches.addAll(matchesWithoutBaseDrink);
        }
        if(screen.equals("alc")){
            alcDrinkScreenManager.receiveMatches(matches);
        } else if (screen.equals("non-alc")) {
            nonAlcDrinkScreenManager.receiveMatches(matches);
        } else if (screen.equals("other")) {

        }

    }

    public void getBaseDrinkCompatibleRecipesFromDatabase(String chosenBaseDrink) {
        ArrayList<Ingredient> ingredients = new ArrayList<>(); //A new ArrayList to store the ingredients for a recipe from the database
        String getRecipeIngredients = "select * from " +
                "ingredients where ingredient_name in (select ingredient_name from " +
                "recipes_ingredients where recipe_name = ?)";

        String getRecipeNames = "Select recipe_name from recipes_ingredients where ingredient_name = ?";

        try (PreparedStatement statementGetNames = connection.prepareStatement(getRecipeNames)) {
            PreparedStatement statmentGetIngredients = connection.prepareStatement(getRecipeIngredients);
            statementGetNames.setString(1, chosenBaseDrink);
            ResultSet resultSet = statementGetNames.executeQuery();
            while (resultSet.next()) {
                String recipeName = resultSet.getString("recipe_name");
                //alcDrinkScreenManager.receiveBaseDrinkMatches(recipeName);
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


    public void checkForAlcRecipe(String chosenIngredientName) {
        Ingredient ingredient = ingredientsController.getIngredientFromArrayList(chosenIngredientName);
        chosenIngredients.add(ingredient);
        checkPartialMatchesIncludingBaseDrink(chosenIngredients);
        checkFullAlcMatches(chosenIngredients);
        sendMatches("alc");
    }

    public void checkForNonAlcRecipe(String chosenIngredientName) {
        Ingredient ingredient = ingredientsController.getIngredientFromArrayList(chosenIngredientName);
        chosenIngredients.add(ingredient);
        checkPartialMatchesOfDrinks(chosenIngredients);
        checkFullNonAlcMatches(chosenIngredients);
        sendMatches("non-alc");
    }

    /**
     * Sets the GUI controller for displaying recipes.
     *
     * @param AlcGUIController The GUI controller for displaying recipes.
     */
    public void setAlcGUI(AlcDrinkScreenManager AlcGUIController) {
        this.alcDrinkScreenManager = AlcGUIController;
    }

    public void setNonAlcGUI(NonAlcDrinkScreenManager NonAlcGUIController) {
        this.nonAlcDrinkScreenManager = NonAlcGUIController;
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