package src.GUI.Boundary;

import src.Client.Controller.RecipeController;

import java.util.ArrayList;

public interface IMainFrame {
    void receiveIngredientsList(ArrayList<String> ingredientNames);
    void receiveRecipeName(String recipeName);
    void receiveRecipeInstructions(ArrayList<String> recipeInstructions);
    void sendChosenDrink(String drinkName);
    void sendChosenIngredients(ArrayList<String> chosenIngredients);
    void setRecipeController(RecipeController recipeController);
}
