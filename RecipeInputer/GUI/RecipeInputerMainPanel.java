package RecipeInputer.GUI;

import src.GUI.Boundary.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class RecipeInputerMainPanel extends JPanel {
    private RecipeInputerMainFrame mainFrame;
    private JTextField recipeNameTextField;
    private InputPanel inputPanel;

    public RecipeInputerMainPanel(RecipeInputerMainFrame mainFrame, int width, int height) {
        this.mainFrame = mainFrame;
        setLayout(null);
        setSize(width, height);
        setBackground(Color.ORANGE);
        ButtonPanel buttonPanel = new ButtonPanel(this);
        buttonPanel.setLocation(width/2-(buttonPanel.addRecipeButton.getWidth()/2), height - 75);
        add(buttonPanel);
        recipeNameTextField = new JTextField("Recipe Name:");
        recipeNameTextField.setSize(100, 30);
        recipeNameTextField.setLocation(100, 30);
        add(recipeNameTextField);
        inputPanel = new InputPanel(this, width, height);
        inputPanel.setLocation(10, 150);
        add(inputPanel);

    }

    public void addRecipeToDatabase() {
        String name;
        HashMap<String, Boolean> ingredients = new HashMap<>();
        name = recipeNameTextField.getText();
        for (int i = 0; i < inputPanel.ingredientNameTextFields.size(); i++){
            ingredients.put(inputPanel.ingredientNameTextFields.get(i).getText(), inputPanel.alcoholicIngredientCheckBox.get(i).isSelected());
        }
        mainFrame.addRecipeToDatabase(name, ingredients);
    }
}

class ButtonPanel extends JPanel {
    private RecipeInputerMainPanel mainPanel;
    protected JButton addRecipeButton;

    public ButtonPanel(RecipeInputerMainPanel mainPanel) {
        this.mainPanel = mainPanel;
        setLayout(null);
        addRecipeButton = new JButton("Add recipe");
        setSize(addRecipeButton.getPreferredSize());
        addRecipeButton.setLocation(0,0);
        addRecipeButton.setSize(addRecipeButton.getPreferredSize());
        add(addRecipeButton);
        addRecipeButton.addActionListener(l -> addRecipeToDatabase());

    }

    private void addRecipeToDatabase() {
        mainPanel.addRecipeToDatabase();
    }
}

class InputPanel extends JPanel{
    private RecipeInputerMainPanel mainPanel;
    protected ArrayList<JTextField> ingredientNameTextFields = new ArrayList<>();
    protected ArrayList<JCheckBox> alcoholicIngredientCheckBox = new ArrayList<>();

    public InputPanel(RecipeInputerMainPanel mainPanel, int width, int height) {
        this.mainPanel = mainPanel;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        setLayout(new GridBagLayout());
        setSize(width/2-50, height-300);
        for (int i = 0; i < 12; i++) {

            ingredientNameTextFields.add(new JTextField(10));
            gridBagConstraints.gridx=0;
            gridBagConstraints.gridy =i;
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            add(ingredientNameTextFields.get(i), gridBagConstraints);
            alcoholicIngredientCheckBox.add(new JCheckBox("Alcoholic?"));
            gridBagConstraints.gridx=1;
            gridBagConstraints.gridy =i;
            add(alcoholicIngredientCheckBox.get(i), gridBagConstraints);
        }
    }
}
