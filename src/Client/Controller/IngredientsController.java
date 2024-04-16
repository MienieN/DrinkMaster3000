package src.Client.Controller;

import src.Client.Entity.Ingredient;

//class that's supposed to get ingredients from database
public class IngredientsController {
    public IngredientsController() {
    }

    public Ingredient getIngredientFromDatabase(String ingridientName) {
        return Ingredient.getIngredient(ingridientName);
    }

    public String[] getIngredientsNameFromDatabase() {
        return Ingredient.getIngredientNames();
    }
}
