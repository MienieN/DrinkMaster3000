package RecipeInputer.GUI;

import src.GUI.Boundary.MainPanel;

import javax.swing.*;

public class RecipeInputerMainFrame extends JFrame {
    private JPanel mainPanel;
    public RecipeInputerMainFrame(int width, int height){
        mainPanel = new RecipeInputerMainPanel();

    }
}
