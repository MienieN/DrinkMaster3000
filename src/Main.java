package src;

import src.Client.Controller.RecipeController;
import src.GUI.Boundary.IMainFrame;
import src.GUI.Boundary.MainFrame;

public class Main {
    public static void main(String[] args){
        IMainFrame mainFrame = new MainFrame();
        RecipeController recipeController = new RecipeController(mainFrame);

    }
}
