package src.Client.Controller;

import src.Client.Entity.Ingredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecipeController {
    private HashMap recipes = new HashMap<ArrayList<Ingredient>, String>();
    private IMainFrame mainFrame;

    public RecipeController(IMainFrame mainFrame){
        this.mainFrame = mainFrame;
        addTestRecipies();
        sendIngredientsToGui(mainFrame);
    }

    private void sendIngredientsToGui(IMainFrame mainFrame) {
        ArrayList<String> ingredientNames = new ArrayList<>(List.of(Ingredient.getIngredientNames()));
        mainFrame.receiveIngredientsList(ingredientNames);

    }

    public void addTestRecipies(){
        //TODO Auto alphabetize recipes
        recipes.put(new ArrayList<Ingredient>(List.of(Ingredient.AngosturaBitters,Ingredient.SimpleSyrup, Ingredient.Whisky)), "Old Fashioned");
        recipes.put(new ArrayList<Ingredient>(List.of(Ingredient.Amaretto,Ingredient.Ice,Ingredient.Orange, Ingredient.Whisky)), "GodFather");
        recipes.put(new ArrayList<Ingredient>(List.of(Ingredient.AngosturaBitters, Ingredient.EggWhite, Ingredient.Lemon, Ingredient.SimpleSyrup, Ingredient.Whisky)), "Whisky Sour");
    }

    public void checkForRecipe(ArrayList<String> chosenIngredientsNames){
        ArrayList<Ingredient> chosenIngredients = new ArrayList<>();
        chosenIngredientsNames.sort(null);
        for (String ingredientName : chosenIngredientsNames){
            chosenIngredients.add(Ingredient.getIngredient(ingredientName));
        }
        if(recipes.containsKey(chosenIngredients)){
            mainFrame.receiveRecipeName((String) recipes.get(chosenIngredients));
        }

    }
}

