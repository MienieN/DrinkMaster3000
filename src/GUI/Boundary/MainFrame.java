package src.GUI.Boundary;

import javax.swing.*;
import java.util.ArrayList;

public class MainFrame extends JFrame implements IMainFrame{
    private MainPanel mainPanel;
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
}
