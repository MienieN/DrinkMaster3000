package src.Client.Controller;

import src.Client.Entity.Ingredient;

import java.sql.*;
import java.util.ArrayList;

//class that's supposed to get ingredients from database
public class IngredientsController {
    private ArrayList<Ingredient> ingredients;
    private Connection connection;

    public IngredientsController() {
        connect();
        getAllIngredientsFromDatabase();
    }

    /**
     * Method that connects to the database
     */
    public void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://pgserver.mau.se:5432/drinkmaster3000", "ao7503", "t360bxdp");
            System.out.println("Connection established");
        } catch (SQLException e) {
            System.out.println("Error in connection");
            throw new RuntimeException(e);
        }
    }

    /**
     * Method that gets all ingredients from the database and puts them in an arraylist of ingredients objects
     * @return An arraylist of ingredients
     */
    public ArrayList<Ingredient> getAllIngredientsFromDatabase() {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        String allIngredients = "SELECT * FROM ingredients"; //query depending on how the alcohol marker is set

        try (PreparedStatement statement = connection.prepareStatement(allIngredients); 
             ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    String ingredientName = resultSet.getString("ingredient_name"); //idk what the column name is
                    Ingredient ingredient = new Ingredient(ingredientName);
                    ingredients.add(ingredient);
            }
                resultSet.close();
                statement.close();

        } catch (SQLException e) {
            System.out.println("Error in getting ingredients from database");
            throw new RuntimeException(e);
        }
        return ingredients;
    }

    /**
     * Method that gets an ingredient from the ArrayList based on the name of the ingredient
     * @param ingredientName The name of the ingredient
     * @return The ingredient object
     */
    public Ingredient getIngredientFromArrayList(String ingredientName) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getName().equals(ingredientName)) {
                return ingredient;
            }
        }
        return null;
    }
}