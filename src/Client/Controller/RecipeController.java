package src.Client.Controller;

import src.Client.Entity.Ingredient;
import src.GUI.Boundary.IMainFrame;

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
        recipes.put(new ArrayList<Ingredient>(List.of(Ingredient.Whisky, Ingredient.AngosturaBitters, Ingredient.SimpleSyrup)), "Old Fashioned");
        recipes.put(new ArrayList<Ingredient>(List.of(Ingredient.Whisky, Ingredient.Orange,Ingredient.Ice, Ingredient.Amaretto)), "GodFather");
        recipes.put(new ArrayList<Ingredient>(List.of(Ingredient.Whisky, Ingredient.AngosturaBitters, Ingredient.SimpleSyrup, Ingredient.Lemon, Ingredient.EggWhite)), "Whisky Sour");
    }

    public void checkForRecipe(ArrayList<String> chosenIngredientsNames){
        ArrayList<Ingredient> chosenIngredients = new ArrayList<>();
        for (String ingredientName : chosenIngredientsNames){
            chosenIngredients.add(Ingredient.getIngredient(ingredientName));
        }
        if(recipes.containsKey(chosenIngredients)){
            mainFrame.showRecipe((String) recipes.get(chosenIngredients));
        }

    }
}

