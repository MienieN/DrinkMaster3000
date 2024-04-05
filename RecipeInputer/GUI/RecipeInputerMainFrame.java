package RecipeInputer.GUI;

import RecipeInputer.RecipeInputerController;
import src.Client.Controller.RecipeController;
import src.GUI.Boundary.MainPanel;

import javax.swing.*;
import java.util.HashMap;

public class RecipeInputerMainFrame extends JFrame {
    private JPanel mainPanel;
    private RecipeInputerController controller;
    public RecipeInputerMainFrame(int width, int height, RecipeInputerController controller){
        this.controller = controller;
        mainPanel = new RecipeInputerMainPanel(this, width, height);
        setSize(width, height);
        add(mainPanel);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void addRecipeToDatabase(String name, HashMap<String, Boolean> ingredients) {
        controller.addRecipe(name, ingredients);
    }
}
