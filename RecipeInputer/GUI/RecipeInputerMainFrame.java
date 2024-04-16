package RecipeInputer.GUI;

import RecipeInputer.RecipeInputerController;


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

    public void addRecipeToDatabase(String name, HashMap<String, Boolean> ingredients, String instructions) {
        controller.addRecipe(name, ingredients, instructions);
    }
}
