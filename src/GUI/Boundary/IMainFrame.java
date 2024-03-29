package src.GUI.Boundary;

import java.util.ArrayList;

public interface IMainFrame {

    void receiveIngredientsList(ArrayList<String> ingredientNames);
    void receiveDrinkName(String drinkName);
    void receiveRecipeInstructions(ArrayList<String> recipeInstructions);
    void sendChosenDrink(String drinkName);
    void sendChosenIngredients(ArrayList<String> chosenIngredients);
    // TODO void setRecipeController(RecipeController recipeController);
    // TODO void setIngriedientsController(IngredientsController ingredientsController);
}
