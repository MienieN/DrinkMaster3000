package src.GUI.Boundary;

import javax.swing.*;
import java.util.ArrayList;

public class MainPanel extends JPanel {
    private MainFrame mainFrame;
    private JButton ingredient1Button = new JButton();
    private JButton ingredient2Button = new JButton();
    private JButton ingredient3Button = new JButton();
    private JButton ingredient4Button = new JButton();
    private int counter = 0;
    private ArrayList<String> ingredientNames;
    public MainPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setSize(mainFrame.getWidth(), mainFrame.getHeight());
        setLayout(null);
        ingredient1Button.setSize(100, 100);
        ingredient2Button.setSize(100, 100);
        ingredient3Button.setSize(100, 100);
        ingredient4Button.setSize(100, 100);
        ingredient1Button.setLocation(100, 100);
        ingredient2Button.setLocation(200, 100);
        ingredient3Button.setLocation(100, 200);
        ingredient4Button.setLocation(200, 200);
        add(ingredient1Button);
        add(ingredient2Button);
        add(ingredient3Button);
        add(ingredient4Button);

    }

    public void receiveIngredientsList(ArrayList<String> ingredientNames) {
        this.ingredientNames = ingredientNames;
        displayNextButtons();
    }

    private void displayNextButtons(JButton button) {
        if(counter >ingredientNames.size()){

        }
        else{
            button.setText(ingredientNames.get(counter++));
        }


    }
}
