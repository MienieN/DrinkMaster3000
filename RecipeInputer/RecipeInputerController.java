package RecipeInputer;

import RecipeInputer.GUI.RecipeInputerMainFrame;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class RecipeInputerController {
    private Connection connection;

    public RecipeInputerController(Connection connection) {
        this.connection = connection;
        new RecipeInputerMainFrame(500, 600, this);
    }

    public void addRecipe(String recipeName, HashMap<String, Boolean> ingredients, String instructions) {
        Savepoint savepoint = null;
        try {
            savepoint = connection.setSavepoint();
        } catch (SQLException e) {
            System.out.println("Failed to connect to database, aborting");
            System.exit(0);
        }
        String insertRecipe = "INSERT INTO recipes values (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertRecipe)) {
            statement.setString(1, recipeName);
            statement.setString(2, instructions);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected != 1) {
                System.out.println("Error: Failed to insert recipe");
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate key value violates unique constraint")) {
                System.out.println("A recipe with that name already exsists");
            } else {
                throw new RuntimeException(e);
            }

        }


        String insertIngredient = "INSERT INTO ingredients values (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertIngredient)) {
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
                } catch (SQLException sqlException) {
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
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            connection.commit();
        } catch (SQLException se) {
            try {
                connection.rollback(savepoint);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
