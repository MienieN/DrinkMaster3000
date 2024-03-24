package src.GUI.Boundary;

import src.Client.Controller.RecipeController;

import java.util.ArrayList;

public interface IMainFrame {
    void receiveIngredientsList(ArrayList<String> ingredientNames);

    void checkForRecipe(ArrayList<String> chosenIngredients);
    void setRecipeController(RecipeController recipeController);

    void showRecipe(String name);
}
