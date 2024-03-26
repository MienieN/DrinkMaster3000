package src.GUI.Boundary;

import java.util.ArrayList;

public interface IMainFrame {

    void receiveIngredientsList(ArrayList<String> ingredientNames);
    void receiveRecipeName(String recipeName);
    void receiveRecipeInstructions(ArrayList<String> recipeInstructions);
    void sendChosenDrink();
    void sendChosenIngredients();
}
