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
    private JTextArea instructionsTextArea;

    public RecipeInputerMainPanel(RecipeInputerMainFrame mainFrame, int width, int height) {
        this.mainFrame = mainFrame;
        setLayout(null);
        setSize(width, height);
        setBackground(Color.ORANGE);
        createLabels();

        ButtonPanel buttonPanel = new ButtonPanel(this);
        buttonPanel.setLocation(width/2-(buttonPanel.addRecipeButton.getWidth()/2), height - 75);
        add(buttonPanel);

        //Textfield to input the name of the recipe
        recipeNameTextField = new JTextField(10);
        recipeNameTextField.setSize(recipeNameTextField.getPreferredSize());
        recipeNameTextField.setLocation(95, 50);
        add(recipeNameTextField);

        //Create a panel to hold the input fields
        inputPanel = new InputPanel(this, width, height);
        inputPanel.setLocation(10, 150);
        add(inputPanel);

        instructionsTextArea = new JTextArea();
        instructionsTextArea.setSize(235, height - 130);
        instructionsTextArea.setLocation(235, 40);
        add(instructionsTextArea);
    }

    public void addRecipeToDatabase() {
        String name;
        String instructions;
        HashMap<String, Boolean> ingredients = new HashMap<>();
        if(!(recipeNameTextField.getText().isBlank() || recipeNameTextField.getText().isEmpty() || recipeNameTextField == null)){
            name = recipeNameTextField.getText();
        }else{
            System.out.println("Recipe name is empty");
            return;
        }

        instructions = instructionsTextArea.getText();
        for (int i = 0; i < inputPanel.ingredientNameTextFields.size(); i++){
            if(!((inputPanel.ingredientNameTextFields.get(i).getText() == null) || (inputPanel.ingredientNameTextFields.get(i).getText().isEmpty()) || (inputPanel.ingredientNameTextFields.get(i).getText().isBlank()))){
                ingredients.put(inputPanel.ingredientNameTextFields.get(i).getText(), inputPanel.alcoholicIngredientCheckBox.get(i).isSelected());
            }else{
                System.out.println("ingredient is empty");
            }

        }
        mainFrame.addRecipeToDatabase(name, ingredients, instructions);
    }

    //Create and add labels for clarity
    public void createLabels(){

        JLabel label = new JLabel("Recipe Name:");
        label.setSize(label.getPreferredSize());
        label.setLocation(15, 50);
        add(label);

        JLabel ingredientColumn = new JLabel("Ingredient Name");
        ingredientColumn.setSize(ingredientColumn.getPreferredSize());
        ingredientColumn.setLocation(20, 130);
        add(ingredientColumn);

        JLabel recipeInstructionsLabel = new JLabel("Recipe Instructions");
        recipeInstructionsLabel.setSize(recipeInstructionsLabel.getPreferredSize());
        recipeInstructionsLabel.setLocation(300, 20);
        add(recipeInstructionsLabel);
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
