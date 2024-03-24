package src.GUI.Boundary;

import src.Client.Controller.RecipeController;

import javax.swing.*;
import java.util.ArrayList;

public class MainFrame extends JFrame implements IMainFrame{
    private MainPanel mainPanel;
    private RecipeController recipeController;
    public MainFrame() {
        this.setSize(800, 800);
        mainPanel = new MainPanel(this);
        add(mainPanel);
        setVisible(true);
    }

    @Override
    public void receiveIngredientsList(ArrayList<String> ingredientNames) {
        mainPanel.receiveIngredientsList(ingredientNames);
    }

    @Override
    public void checkForRecipe(ArrayList<String> chosenIngredients) {
        recipeController.checkForRecipe(chosenIngredients);
    }

    @Override
    public void setRecipeController(RecipeController recipeController) {
        this.recipeController = recipeController;
    }

    @Override
    public void showRecipe(String name) {
        mainPanel.showRecipe(name);
    }
}
