package RecipeInputer;

import RecipeInputer.GUI.RecipeInputerMainFrame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.HashMap;

public class RecipeInputerController {
    private Connection connection;

    public RecipeInputerController(Connection connection) {
        this.connection = connection;
        new RecipeInputerMainFrame(500, 600, this);
    }

    public void addRecipe(String recipeName, HashMap<String, Boolean> ingredients) {
        Savepoint savepoint = null;
        try {
            savepoint = connection.setSavepoint();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql = "INSERT INTO recipes values (?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, recipeName);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected != 1) {
                System.out.println("Error: Failed to insert recipe");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String sql2 = "INSERT INTO ingredients values (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql2)) {
            // Loop through the array and insert each element individually
            for (String ingredient : ingredients.keySet()) {
                // Set the array element as a parameter in the prepared statement
                statement.setString(1, ingredient);
                statement.setBoolean(2, ingredients.get(ingredient));

                // Execute the SQL INSERT statement for each array element
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected != 1) {
                    System.err.println("Error: Failed to insert ingredient");
                    // Rollback to the savepoint
                    connection.rollback(savepoint);
                }
            }

            // Commit the transaction if all inserts were successful
            connection.commit();
            System.out.println("Array elements inserted successfully.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
