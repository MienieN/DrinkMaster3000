package RecipeInputer.GUI;

import src.GUI.Boundary.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class RecipeInputerMainPanel extends JPanel {
    private RecipeInputerMainFrame mainFrame;
    private JTextField recipeNameTextField;
    private ArrayList<JTextField> ingredientNameTextFields = new ArrayList<>();
    private ArrayList<JCheckBox> alcoholicIngredientCheckBox = new ArrayList<>();

    public RecipeInputerMainPanel(RecipeInputerMainFrame mainFrame, int width, int height) {
        this.mainFrame = mainFrame;
        setLayout(new GridLayout());
        setSize(width, height);
        recipeNameTextField = new JTextField("Recipe Name:");
        add(recipeNameTextField);
        for (int i = 0; i < 4; i++) {
            ingredientNameTextFields.add(new JTextField("Ingredient Name"));
            add(ingredientNameTextFields.get(i));
            alcoholicIngredientCheckBox.add(new JCheckBox("Alcoholic?"));
            add(alcoholicIngredientCheckBox.get(i));
        }
        ButtonPanel buttonPanel = new ButtonPanel(this);
        add(buttonPanel);
    }

    public void addRecipeToDatabase() {
        String name;
        HashMap<String, Boolean> ingredients = new HashMap<>();
        name = recipeNameTextField.getText();
        for (int i = 0; i < ingredientNameTextFields.size(); i++){
            ingredients.put(ingredientNameTextFields.get(i).getText(), alcoholicIngredientCheckBox.get(i).isSelected());
        }
        mainFrame.addRecipeToDatabase(name, ingredients);
    }
}

class ButtonPanel extends JPanel {
    private RecipeInputerMainPanel mainPanel;
    private JButton addRecipeButton;

    public ButtonPanel(RecipeInputerMainPanel mainPanel) {
        this.mainPanel = mainPanel;
        addRecipeButton = new JButton("Add recipe");
        add(addRecipeButton);
        addRecipeButton.addActionListener(l -> addRecipeToDatabase());
    }

    private void addRecipeToDatabase() {
        mainPanel.addRecipeToDatabase();
    }
}
