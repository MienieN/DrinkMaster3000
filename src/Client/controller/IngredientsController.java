package src.Client.controller;

import src.Client.entity.Ingredient;

import java.sql.*;
import java.util.ArrayList;

/**
 * The IngredientsController class manages interactions with ingredients in the database.
 */
public class IngredientsController {
    private ArrayList<Ingredient> ingredients;      // The list of ingredients
    private Connection connection;                  // The database connection

    /**
     * Constructs an IngredientsController object and initializes the database connection.
     */
    public IngredientsController() {
        // Establish database connection
        connect(); //TODO do this in ClientMain and send it along
        // Retrieve all ingredients from the database
        getAllIngredientsFromDatabase();
    }

    /**
     * Establishes a connection to the database.
     */
    //TODO move this into main and send the connection to the controllers
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
     * Receives all ingredients from the database and puts them in an arraylist of ingredients objects
     *
     * @return An arraylist of ingredients
     */
    //TODO either get 2 lists or sort the list based on alc content
    public void getAllIngredientsFromDatabase() {
        ingredients = new ArrayList<>();
        String allIngredients = "SELECT ingredient_name FROM ingredients"; //query depending on how the alcohol marker is set

        try (PreparedStatement statement = connection.prepareStatement(allIngredients);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    String ingredientName = resultSet.getString(i);
                    Ingredient ingredient = new Ingredient(ingredientName);
                    ingredients.add(ingredient);
                }

            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            System.out.println("Error in getting ingredients from database");
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
        for (Ingredient ingredient : ingredients) {
            ingredientNames.add(ingredient.getName());
        }
        return ingredientNames;
    }
}