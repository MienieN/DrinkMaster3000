package src.GUI.Boundary;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainPanel extends JPanel {
    private MainFrame mainFrame;
    private final JButton[] ingredientButtons = new JButton[4];
    private ArrayList<String> chosenIngredients = new ArrayList<>();

    private ArrayList<String> recipes;
    private JLabel label;
    //private JScrollPane recipesPane;
    private JList<String> recipesList = new JList<>();

    //TODO look into a prettier way to do action listener
    private ActionListener ingredientButtonPressed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton =(JButton)e.getSource();
            chosenIngredients.add(clickedButton.getText());
            System.out.println(clickedButton.getText());
            mainFrame.checkForRecipe(chosenIngredients);
            displayNextButtons(clickedButton);
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
        JButton nOTA = new JButton("None of the above");
        nOTA.setSize(100, 100);
        nOTA.setLocation(150, 300);
        nOTA.addActionListener(l -> noneOfTheAbove());
        add(nOTA);
        JScrollPane recipesPane = new JScrollPane(recipesList);
        recipesPane.setLocation(400,50);
        recipesPane.setSize(100,400);
        add(recipesPane);
        /*label = new JLabel();
        label.setSize(100, 100);
        label.setLocation(400, 150);
        add(label);*/
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

    private void noneOfTheAbove(){
        for (JButton button : ingredientButtons){
            displayNextButtons(button);
        }
    }

    public void showRecipe(String name) {
        if (recipes == null){
            recipes = new ArrayList<String>();
            recipes.add(name);
        }else{
            recipes.add(name);
        }
        //TODO find a prettier way to do this
        String[] stringArray = recipes.toArray(new String[recipes.size()]);
        recipesList.setListData(stringArray);
        //recipesList.setListData(recipes.toArray()); for some reason this won't work
        repaint();
    }
}
