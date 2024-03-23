package src.Client.Controller;

import src.Client.Entity.Ingredient;
import src.GUI.Boundary.IMainFrame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecipeController {
    private HashMap recipies = new HashMap<ArrayList<Ingredient>, String>();

    public RecipeController(IMainFrame mainFrame){
        addTestRecipies();
        sendIngredientsToGui(mainFrame);
    }

    private void sendIngredientsToGui(IMainFrame mainFrame) {
        ArrayList<String> ingredientNames = new ArrayList<>(List.of(Ingredient.getIngredientNames()));
        mainFrame.receiveIngredientsList(ingredientNames);

    }

    public void addTestRecipies(){
        recipies.put(new ArrayList<Ingredient>(List.of(Ingredient.Whisky, Ingredient.AngosturaBitters, Ingredient.SimpleSyrup)), "Whisky Sour");
        recipies.put(new ArrayList<Ingredient>(List.of(Ingredient.Whisky, Ingredient.Orange,Ingredient.Ice, Ingredient.Amaretto)), "GodFather");
        recipies.put(new ArrayList<Ingredient>(List.of(Ingredient.Whisky, Ingredient.AngosturaBitters, Ingredient.Lemon, Ingredient.SimpleSyrup, Ingredient.EggWhite)), "Whisky Sour");
    }
}

