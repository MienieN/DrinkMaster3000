package src.client.controller;

import src.client.entity.Ingredient;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * The IngredientsController class manages interactions with ingredients in the database.
 */
public class IngredientsController {
    private ArrayList<Ingredient> ingredients;          // The list of ingredients
    private ArrayList<Ingredient> relevantIngredients;  //the list of ingredients sorted by compatability with your base drink
    private Connection connection;                      // The database connection

    /**
     * Constructs an IngredientsController object and initializes the database connection.
     */
    public IngredientsController(Connection connection) {
        this.connection = connection;
        // Retrieve all ingredients from the database
        getAllIngredientsFromDatabase();
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

        try (PreparedStatement statement = connection.prepareStatement(allIngredients)){
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
            } catch(SQLException e){
                throw new RuntimeException(e);
            }

        }

        /**
         * Retrieves an ingredient object from the ArrayList based on the name of the ingredient.
         *
         * @param ingredientName The name of the ingredient.
         * @return The Ingredient object corresponding to the given name, or null if not found.
         */
        public Ingredient getIngredientFromArrayList (String ingredientName){
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
        public ArrayList<String> getIngredientNames () {
            ArrayList<String> ingredientNames = new ArrayList<>();
            for (Ingredient ingredient : relevantIngredients) {
                ingredientNames.add(ingredient.getName());
            }
            return ingredientNames;
        }

        public ArrayList<String> getAlcoholicIngredientNames () {
            ArrayList<String> ingredientNames = new ArrayList<>();
            for (Ingredient ingredient : ingredients) {
                if (ingredient.getAlcoholic()) {
                    ingredientNames.add(ingredient.getName());
                }
            }
            return ingredientNames;
        }

        public ArrayList<String> getNonAlcoholicIngredientNames () {
            ArrayList<String> ingredientNames = new ArrayList<>();
            for (Ingredient ingredient : ingredients) {
                if (!ingredient.getAlcoholic()) {
                    ingredientNames.add(ingredient.getName());
                }
            }
            return ingredientNames;
        }
    }