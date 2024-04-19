package AddRecipe;

import AddRecipe.GUI.AddRecipeMainFrame;

import java.sql.*;
import java.util.HashMap;

/**
 * The AddRecipeController class manages the addition of recipes to the database.
 * It interacts with the database to insert recipe details and related ingredients.
 */
public class AddRecipeController {
    private Connection connection;  // Connection to the database

    /**
     * Constructs a new AddRecipeController with the specified database connection.
     *
     * @param connection The Connection object representing the database connection.
     */
    public AddRecipeController(Connection connection) {
        this.connection = connection;
        new AddRecipeMainFrame(500, 600, this);
    }

    /**
     * Adds a new recipe to the database with the given name, ingredients, and instructions.
     *
     * @param recipeName  The name of the recipe to be added.
     * @param ingredients A HashMap containing the ingredients of the recipe and their alcoholic status.
     * @param instructions The instructions for preparing the recipe.
     */
    public void addRecipe(String recipeName, HashMap<String, Boolean> ingredients, String instructions) {
        // Set a savepoint for transaction rollback
        Savepoint savepoint = null;
        try {
            savepoint = connection.setSavepoint();
        }
        catch (SQLException e) {
            System.out.println("Failed to connect to database, aborting");
            System.exit(0);
        }

        // Insert recipe details into the 'recipes' table
        String insertRecipe = "INSERT INTO recipes values (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertRecipe)) {
            statement.setString(1, recipeName);
            statement.setString(2, instructions);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected != 1) {
                System.out.println("Error: Failed to insert recipe");
            }
        }
        catch (SQLException e) {
            // Handle duplicate recipe name error
            if (e.getMessage().contains("duplicate key value violates unique constraint")) {
                System.out.println("A recipe with that name already exsists");
            } else {
                throw new RuntimeException(e);
            }
        }

        // Insert ingredients into the 'ingredients' table
        String insertIngredient = "INSERT INTO ingredients values (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertIngredient)) {
            // Set a savepoint for each ingredient insertion
            Savepoint ingredientSavepoint;

            // Loop through the array and insert each element individually
            for (String ingredient : ingredients.keySet()) {
                ingredientSavepoint = connection.setSavepoint();
                try {
                    statement.setString(1, ingredient);
                    statement.setBoolean(2, ingredients.get(ingredient));

                    // Execute the SQL INSERT statement for each array element
                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected != 1) {
                        System.err.println("Error: Failed to insert ingredient");
                        try {
                            connection.rollback();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        System.out.println("test");
                    }
                }
                // Handle duplicate ingredient name error
                catch (SQLException sqlException) {
                    if (sqlException.getMessage().contains("duplicate key value violates unique constraint")) {
                        System.out.println("An ingredient with that name already exsists, skipping");
                        connection.rollback(ingredientSavepoint);
                    } else {
                        throw new RuntimeException(sqlException);
                    }
                }
            }
            System.out.println("Array elements inserted successfully.");
        } catch (SQLException se) {
            throw new RuntimeException(se);
        }

        // Insert recipe-ingredient connections into the 'recipes_ingredients' table
        String insertRecipesIngredients = "INSERT INTO recipes_ingredients values (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertRecipesIngredients)) {
            statement.setString(1, recipeName);

            for (String ingredient : ingredients.keySet()) {
                statement.setString(2, ingredient);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected != 1) {
                    System.err.println("Error: Failed to insert recipe to ingredient connection");
                    try {
                        connection.rollback();
                    }
                    catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            // Commit the transaction
            connection.commit();
        }

        // Rollback transaction in case of error
        catch (SQLException se) {
            try {
                connection.rollback(savepoint);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
