package addRecipe.gui;

import addRecipe.AddRecipeController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The AddRecipeMainPanel class represents the main panel of the "Add Recipe" GUI.
 * This panel contains input fields for adding a new recipe, including name, ingredients, and instructions.
 */
public class AddRecipeMainPanel extends JPanel {
    private AddRecipeMainFrame mainFrame;       // The main frame of the "Add Recipe" GUI
    private JTextField recipeNameTextField;     // Text field for entering the name of the recipe
    private InputPanel inputPanel;              // Panel containing input fields for ingredients
    private JTextArea instructionsTextArea;     // Text area for entering recipe instructions
    private AddRecipeController controller;

    /**
     * Constructs a new AddRecipeMainPanel with the specified main frame, width, and height.
     *
     * @param mainFrame The main frame of the "Add Recipe" GUI.
     * @param width     The width of the main panel.
     * @param height    The height of the main panel.
     */
    public AddRecipeMainPanel(AddRecipeMainFrame mainFrame, int width, int height) {
        // general layout
        this.mainFrame = mainFrame;
        setLayout(null);
        setSize(width, height);
        setBackground(Color.ORANGE);

        // create and position labels
        createLabels();

        // create and position button panel
        ButtonPanel buttonPanel = new ButtonPanel(this);
        buttonPanel.setLocation(width/2-(buttonPanel.addRecipeButton.getWidth()/2), height - 75);
        add(buttonPanel);

        // Text field to input the name of the recipe
        recipeNameTextField = new JTextField(10);
        recipeNameTextField.setSize(recipeNameTextField.getPreferredSize());
        recipeNameTextField.setLocation(95, 50);
        add(recipeNameTextField);

        // Create a panel to hold the input fields for the ingredients
        inputPanel = new InputPanel(this, width, height);
        inputPanel.setLocation(10, 150);
        add(inputPanel);

        // Text area for entering recipe instructions
        instructionsTextArea = new JTextArea();
        instructionsTextArea.setSize(235, height - 130);
        instructionsTextArea.setLocation(235, 40);
        add(instructionsTextArea);
    }

    /**
     * Adds the entered recipe to the database by retrieving information from the input fields.
     */
    public void addRecipeToDatabase() {
        String name;
        String instructions;
        HashMap<String, Boolean> ingredients = new HashMap<>();
        if (!(recipeNameTextField.getText().isBlank() || recipeNameTextField.getText().isEmpty() || recipeNameTextField == null)) {
            name = recipeNameTextField.getText();
        } else {
            System.out.println("Recipe name is empty");
            return;
        }

        instructions = instructionsTextArea.getText();
        for (int i = 0; i < inputPanel.ingredientNameTextFields.size(); i++) {
            if (!((inputPanel.ingredientNameTextFields.get(i).getText() == null) || (inputPanel.ingredientNameTextFields.get(i).getText().isEmpty()) || (inputPanel.ingredientNameTextFields.get(i).getText().isBlank()))) {
                ingredients.put(inputPanel.ingredientNameTextFields.get(i).getText(), inputPanel.alcoholicIngredientCheckBox.get(i).isSelected());
            } else {
                System.out.println("ingredient is empty");
            }
        }
        mainFrame.addRecipeToDatabase(name, ingredients, instructions);
    }

    /**
     * Create and add labels for clarity.
     */
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

    public AddRecipeController getController() {
        return this.controller;
    }
}

/**
 * The ButtonPanel class represents the panel containing buttons for the "Add Recipe" GUI.
 * This panel includes a button for adding a recipe.
 */
class ButtonPanel extends JPanel {
    private AddRecipeMainPanel mainPanel;   // The main panel of the "Add Recipe" GUI
    protected JButton addRecipeButton;      // Button for adding a recipe

    /**
     * Constructs a new ButtonPanel with the specified main panel.
     *
     * @param mainPanel The main panel of the "Add Recipe" GUI.
     */
    public ButtonPanel(AddRecipeMainPanel mainPanel) {
        // Set layout
        this.mainPanel = mainPanel;
        setLayout(null);

        // Create and add: add recipe button
        addRecipeButton = new JButton("Add recipe");
        setSize(addRecipeButton.getPreferredSize());
        addRecipeButton.setLocation(0,0);
        addRecipeButton.setSize(addRecipeButton.getPreferredSize());
        add(addRecipeButton);

        // Add action listener to the add recipe button
        addRecipeButton.addActionListener(l -> addRecipeToDatabase());
    }

    /**
     * Adds the entered recipe to the database by calling the appropriate method in the main panel.
     */
    private void addRecipeToDatabase() {
        mainPanel.addRecipeToDatabase();
    }
}

/**
 * The InputPanel class represents the panel containing input fields for recipe ingredients.
 * This panel includes text fields for ingredient names and checkboxes for indicating alcoholic ingredients.
 */
class InputPanel extends JPanel{
    private AddRecipeMainPanel mainPanel;                                           // The main panel of the "Add Recipe" GUI
    protected ArrayList<JTextField> ingredientNameTextFields = new ArrayList<>();   // List of text fields for ingredient names
    protected ArrayList<JCheckBox> alcoholicIngredientCheckBox = new ArrayList<>(); // List of checkboxes for indicating alcoholic ingredients
    protected ArrayList<JComboBox> ingredientSuggestionComboboxes = new ArrayList<>();
    /**
     * Constructs a new InputPanel with the specified main panel, width, and height.
     *
     * @param mainPanel The main panel of the "Add Recipe" GUI.
     * @param width     The width of the input panel.
     * @param height    The height of the input panel.
     */
    public InputPanel(AddRecipeMainPanel mainPanel, int width, int height) {
        this.mainPanel = mainPanel;


        // Set layout to grid bag layout for organized positioning of components
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        setLayout(new GridBagLayout());
        setSize(width/2-50, height-300);

        for (int i = 0; i < 12; i++) {
            // Create text field for ingredient name
            JTextField ingredientNameTextField = new JTextField(10);
            ingredientNameTextFields.add(ingredientNameTextField);//new JTextField(10));
            gridBagConstraints.gridx=0;
            gridBagConstraints.gridy =i;
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            //add(ingredientNameTextFields.get(i), gridBagConstraints);
            add(ingredientNameTextField, gridBagConstraints);

            // Create checkbox for indicating alcoholic ingredient
            alcoholicIngredientCheckBox.add(new JCheckBox("Alcoholic?"));
            gridBagConstraints.gridx=1;
            gridBagConstraints.gridy =i;
            add(alcoholicIngredientCheckBox.get(i), gridBagConstraints);

            //Create combox for suggestions
            JComboBox<String> ingredientCombobox = new JComboBox<>();
            ingredientSuggestionComboboxes.add(ingredientCombobox);
            ingredientCombobox.setEditable(false);
            ingredientCombobox.setSize(ingredientNameTextField.getPreferredSize());
            ingredientCombobox.setVisible(false);
            gridBagConstraints.gridx=1;
            gridBagConstraints.gridy =i;
            add(ingredientCombobox, gridBagConstraints);

            // Add a document listener on the ingredient text field
            ingredientNameTextField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateSuggestions(ingredientNameTextField, ingredientCombobox);

                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateSuggestions(ingredientNameTextField, ingredientCombobox);

                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    updateSuggestions(ingredientNameTextField, ingredientCombobox);

                }
            });

            ingredientCombobox.addActionListener(e -> {
                if(ingredientCombobox.getSelectedItem() != null) {
                    ingredientNameTextField.setText(ingredientCombobox.getSelectedItem().toString());
                    ingredientCombobox.setVisible(false);
                }
            });
        }
    }
    public void updateSuggestions(JTextField textField, JComboBox comboBox) {
        String searchText = textField.getText();
        comboBox.removeAllItems();

        if (!searchText.isEmpty()) {
            String temp = searchText.substring(0, 1).toUpperCase() + searchText.substring(1);
            searchText = temp;

            if (mainPanel != null && mainPanel.getController() != null) {
                for (String suggestion : mainPanel.getController().queryIngredientsName(searchText)) {
                    comboBox.addItem(suggestion);
                }
                if (comboBox.getItemCount() > 0) {
                    comboBox.setVisible(true);
                    comboBox.showPopup();
                } else {
                 comboBox.setVisible(false);
                }
            }
            else {
                System.out.println("Controller is null noob");
            }
        } else {
            comboBox.setVisible(false);
        }
    }

/*
    private List getMatchingIngredientSuggestions (String input) {
        List<String> matchingIngredients = new ArrayList<>();


    }

        if (!(searchText == null || searchText.equals(""))) {
            String temp = searchText.substring(0, 1).toUpperCase() + searchText.substring(1);
            searchText = temp;
        }

        System.out.println(searchText);
        comboBox.removeAllItems();

        for (String suggestion : mainPanel.getController().queryIngredientsName(searchText)) {
            comboBox.addItem(suggestion);
        }


 */
}