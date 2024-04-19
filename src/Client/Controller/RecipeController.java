package src.Client.Controller;

import src.Client.Boundary.guiClasses.GUIAlcDrinkScreenController;
import src.Client.Entity.Ingredient;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class RecipeController {
    private HashMap<String, ArrayList<Ingredient>> recipes = new HashMap<>();
    private GUIAlcDrinkScreenController GUIController;
    private ArrayList<Ingredient> chosenIngredients = new ArrayList<>();
    private Connection connection;

    public RecipeController() {
        connect();
        getRecipesFromDatabase();
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://pgserver.mau.se:5432/drinkmaster3000", "ao7503", "t360bxdp");
            System.out.println("Connection established");
        } catch (SQLException e) {
            System.out.println("Error in connection");
            throw new RuntimeException(e);
        }
    }

    private void getRecipesFromDatabase() {
        String sql = "Select recipe_name from recipes";
        String sql2 = "Select ingredient_name from recipes_ingredients where recipe_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet recipeNames = statement.executeQuery();
            PreparedStatement statement2 = connection.prepareStatement(sql2);
            while (recipeNames.next()) {
                for (int i = 1; i <= recipeNames.getMetaData().getColumnCount(); i++) {
                    ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
                    String recipeName = recipeNames.getString(i);
                    statement2.setString(1, recipeName);
                    ResultSet resultSet2 = statement2.executeQuery();
                    while (resultSet2.next()) {
                        for (int j = 1; j <= resultSet2.getMetaData().getColumnCount(); j++) {
                            ingredientArrayList.add(new Ingredient(resultSet2.getString(j)));
                        }
                    }
                    recipes.put(recipeName, ingredientArrayList);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public void checkForRecipe(String chosenIngredientName) {
        chosenIngredients.add(new Ingredient(chosenIngredientName));
        for (String recipeName : recipes.keySet()) {
            for (Ingredient ingredient : recipes.get(recipeName)) {

                for (int i = 0; i < chosenIngredients.size(); i++) {
                    if (Objects.equals(chosenIngredients.get(i).getName(), ingredient.getName())) {
                        System.out.println("test");
                        GUIController.receiveRecipeName(recipeName);
                    }
                }
            }
        }
    }

    public void setGUI(GUIAlcDrinkScreenController GUIController) {
        this.GUIController = GUIController;
    }
}

