package src.admin.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MockAdminController extends AdminController {

    /**
     * Constructs a new AddRecipeController with the specified database connection.
     *
     * @param connection The Connection object representing the database connection.
     */
    public MockAdminController(Connection connection) {
        super(null);
    }

    @Override
    public List<String> queryRecipeName(String textSearch) {
        //simulating sample data
        List<String> mockRecipes = new ArrayList<>();
        if (textSearch.startsWith("test")) {
            mockRecipes.add("test recipe 1");
            mockRecipes.add("test recipe 2");
        }
        return mockRecipes;
    }

    public List<String> queryIngredientName(String textSearch) {
        List<String> mockIngredients = new ArrayList<>();
        if (textSearch.startsWith("test")) {
            mockIngredients.add("test ingredient 1");
            mockIngredients.add("test ingredient 2");
        }
        return mockIngredients;
    }

    @Override
    public void addRecipe(String recipeName, HashMap<String, Boolean> ingredients, String instructions, Boolean speciality) {
        System.out.println("Mock addRecipe called with");
        System.out.println("recipeName: " + recipeName);
        System.out.println("ingredients: " + ingredients);
        System.out.println("instructions: " + instructions);
        System.out.println("speciality: " + speciality);
    }
}
