package src.GUI.Boundary;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainPanel extends JPanel {
    private MainFrame mainFrame;
    private JButton[] ingredientButtons = new JButton[4];

    //TODO look into a prettier way to do action listener
    private ActionListener ingredientButtonPressed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            displayNextButtons((JButton)e.getSource());
        }
    };
    private int counter = 0;
    private ArrayList<String> ingredientNames;
    public MainPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setSize(mainFrame.getWidth(), mainFrame.getHeight());
        setLayout(null);
        for (int i = 0; i < ingredientButtons.length; i++) {
            ingredientButtons[i] = new JButton();
            ingredientButtons[i].setSize(100, 100);
            ingredientButtons[i].setName(String.valueOf(i));
            ingredientButtons[i].addActionListener(ingredientButtonPressed);
        }

        //TODO make a better way to set location of buttons
        ingredientButtons[0].setLocation(100, 100);
        ingredientButtons[1].setLocation(200, 100);
        ingredientButtons[2].setLocation(100, 200);
        ingredientButtons[3].setLocation(200, 200);
        for (int i = 0; i < ingredientButtons.length; i++) {
            add(ingredientButtons[i]);
        }
    }

    public void receiveIngredientsList(ArrayList<String> ingredientNames) {
        this.ingredientNames = ingredientNames;
        for (JButton button : ingredientButtons){
            displayNextButtons(button);
        }

    }

    private void displayNextButtons(JButton button) {
        if(counter >= ingredientNames.size()){
            System.out.println("Reached end of ingredient list");
        }
        else{
            button.setText(ingredientNames.get(counter++));
        }


    }
}
