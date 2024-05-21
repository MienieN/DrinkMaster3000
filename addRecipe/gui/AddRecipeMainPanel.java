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
    // The main frame of the "Add Recipe" GUI
    private AddRecipeMainFrame mainFrame;
    // Text field for entering the name of the recipe
    private JTextField recipeNameTextField;
    // Panel containing input fields for ingredients
    private InputPanel inputPanel;
    // Text area for entering recipe instructions
    private JTextArea instructionsTextArea;
    // Combobox for recipe name suggestions
    private JComboBox<Object> recipeNameSuggestions = new JComboBox<>();
    // Checkbox for indicating speciality recipes
    private JCheckBox specialitiesCheckBox;


    /**
     * Constructs a new AddRecipeMainPanel with the specified main frame, width, and height.
     *
     * @param mainFrame The main frame of the "Add Recipe" GUI.
     * @param width     The width of the main panel.
     * @param height    The height of the main panel.
     */
    public AddRecipeMainPanel(AddRecipeMainFrame mainFrame, int width, int height) {
        // General layout
        this.mainFrame = mainFrame;
        setLayout(null);
        setSize(width, height);
        setBackground(Color.ORANGE);

        // Create and position labels
        createLabels();

        // Create and position button panel
        ButtonPanel buttonPanel = new ButtonPanel(this);
        buttonPanel.setLocation(width / 2 - (buttonPanel.addRecipeButton.getWidth() / 2), height - 75);
        add(buttonPanel);

        // Text field to input the name of the recipe
        recipeNameTextField = new JTextField(10);
        recipeNameTextField.setSize(recipeNameTextField.getPreferredSize());
        recipeNameTextField.setLocation(95, 50);
        add(recipeNameTextField);

        JCheckBox specialityRecipeCheckbox = new JCheckBox("Speciality Recipe?");
        specialityRecipeCheckbox.setSelected(false);
        specialityRecipeCheckbox.setLocation(95, 100);
        add(specialityRecipeCheckbox);

        // To dynamically respond to changes in the recipeNameTextField
        DocumentListener docListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateSuggestions();
                recipeNameSuggestions.showPopup();
            }

            public void removeUpdate(DocumentEvent e) {
                updateSuggestions();
                recipeNameSuggestions.showPopup();
            }

            public void changedUpdate(DocumentEvent e) {
                updateSuggestions();
                recipeNameSuggestions.showPopup();
            }
        };

        // Combobox for recipe name suggestions
        recipeNameSuggestions.setEditable(true);
        recipeNameSuggestions.setSize(recipeNameTextField.getSize());
        recipeNameSuggestions.setLocation(95, 80);
        DefaultComboBoxModel<Object> recipeNameSuggestionsModel = new DefaultComboBoxModel<>();
        recipeNameSuggestions.setModel(recipeNameSuggestionsModel);
        ((JTextField) recipeNameSuggestions.getEditor().getEditorComponent())
                .getDocument().addDocumentListener(docListener);
        add(recipeNameSuggestions);

        // Create a panel to hold the input fields for the ingredients
        inputPanel = new InputPanel(this, width, height);
        inputPanel.setLocation(10, 150);
        add(inputPanel);

        // Text area for entering recipe instructions
        instructionsTextArea = new JTextArea();
        instructionsTextArea.setSize(235, height - 130);
        instructionsTextArea.setLocation(235, 40);
        add(instructionsTextArea);

        // Checkbox for indicating speciality recipes
        specialitiesCheckBox = new JCheckBox("Speciality?");
        specialitiesCheckBox.setSize(recipeNameTextField.getSize());
        specialitiesCheckBox.setLocation(95, 100);
        add(specialitiesCheckBox);
    }

    /**
     * Updates the suggestions in the recipe name combobox based on the current text in the recipe name text field.
     */
    private void updateSuggestions() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String searchText = recipeNameTextField.getText();

                if (!(searchText == null || searchText.equals(""))) {
                    String temp = searchText.substring(0, 1).toUpperCase() + searchText.substring(1);
                    searchText = temp;
                }

                System.out.println(searchText);
                recipeNameSuggestions.removeAllItems();

                for (String suggestion : mainFrame.getController().queryRecipeName(searchText)) {
                    recipeNameSuggestions.addItem(suggestion);
                }
            }
        };
        SwingUtilities.invokeLater(runnable);
    }

    /**
     * Adds the entered recipe to the database by retrieving information from the input fields.
     */
    public void addRecipeToDatabase() {
        String name;
        Boolean speciality;
        String instructions;
        HashMap<String, Boolean> ingredients = new HashMap<>();

        if (!(recipeNameTextField.getText().isBlank() || recipeNameTextField.getText().isEmpty()
                || recipeNameTextField == null)) {
            name = recipeNameTextField.getText();
            speciality = specialitiesCheckBox.isSelected();
        } else {
            System.out.println("Recipe name is empty");
            return;
        }

        instructions = instructionsTextArea.getText();
        for (int i = 0; i < inputPanel.ingredientNameTextFields.size(); i++) {
            if (!((inputPanel.ingredientNameTextFields.get(i).getText() == null)
                    || (inputPanel.ingredientNameTextFields.get(i).getText().isEmpty())
                    || (inputPanel.ingredientNameTextFields.get(i).getText().isBlank()))) {

                ingredients.put(inputPanel.ingredientNameTextFields.get(i).getText(),
                        inputPanel.alcoholicIngredientCheckBox.get(i).isSelected());

            } else {
                System.out.println("ingredient is empty");
            }
        }
        mainFrame.addRecipeToDatabase(name, ingredients, instructions, speciality);
    }

    /**
     * Create and add labels for clarity.
     */
    public void createLabels() {
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

    /**
     * Gets the controller for managing interactions with the recipe database.
     *
     * @return The AddRecipeController instance.
     */
    public AddRecipeController getController() {
        return mainFrame.getController();
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
        addRecipeButton.setLocation(0, 0);
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
class InputPanel extends JPanel {
    private AddRecipeMainPanel mainPanel;                                           // The main panel of the "Add Recipe" GUI
    private ArrayList<JComboBox> ingredientComboBoxes = new ArrayList<>();          // List of comboboxes for ingredient names
    protected ArrayList<JTextField> ingredientNameTextFields = new ArrayList<>();   // List of text fields for ingredient names
    protected ArrayList<JCheckBox> alcoholicIngredientCheckBox = new ArrayList<>(); // List of checkboxes for indicating alcoholic ingredients

    /**
     * Document listener for the ingredient name text fields.
     */
    private DocumentListener documentListener = new DocumentListener() {
        public void insertUpdate(DocumentEvent e) {
            int index = (int) e.getDocument().getProperty("index");
            updateSuggestions(index);
            ingredientComboBoxes.get(index).showPopup();
        }

        /**
         * Removes the update from the document.
         * @param e the document event
         */
        public void removeUpdate(DocumentEvent e) {
            int index = (int) e.getDocument().getProperty("index");
            updateSuggestions(index);
            ingredientComboBoxes.get(index).showPopup();
        }

        /**
         * Changes the update of the document.
         *
         * @param e The document event
         */
        public void changedUpdate(DocumentEvent e) {
            int index = (int) e.getDocument().getProperty("index");
            updateSuggestions(index);
            ingredientComboBoxes.get(index).showPopup();
        }
    };

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
        setSize(width / 2 - 50, height - 300);

        DefaultComboBoxModel<String> ingredientNameSuggestionsModel = new DefaultComboBoxModel<>();

        for (int i = 0; i < 12; i++) {
            // Create text field for ingredient name
            ingredientNameTextFields.add(new JTextField(10));
            ingredientNameTextFields.get(i).getDocument().putProperty("index", i);
            ingredientNameTextFields.get(i).getDocument().addDocumentListener(documentListener);

            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = i;
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            add(ingredientNameTextFields.get(i), gridBagConstraints);

            ingredientComboBoxes.add(new JComboBox<>());
            ingredientComboBoxes.get(i).setSize(ingredientNameTextFields.get(i).getSize());
            ingredientComboBoxes.get(i).setModel(ingredientNameSuggestionsModel);
            add(ingredientComboBoxes.get(i), gridBagConstraints);

            // Create checkbox for indicating alcoholic ingredient
            alcoholicIngredientCheckBox.add(new JCheckBox("Alcoholic?"));
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = i;
            add(alcoholicIngredientCheckBox.get(i), gridBagConstraints);
        }
    }

    /**
     * Updates the suggestions in the ingredient name combobox
     * based on the current text in the ingredient name text field.
     *
     * @param index The index of the ingredient name text field.
     */
    //TODO currently takes all indexes at once, needs to be only 1, implement with comboxes
    private void updateSuggestions(int index) {
        String searchText = ingredientNameTextFields.get(index).getText();

        if (!(searchText == null || searchText.equals(""))) {
            String temp = searchText.substring(0, 1).toUpperCase() + searchText.substring(1);
            searchText = temp;
        }

        System.out.println(searchText);
        ingredientComboBoxes.get(index).removeAllItems();

        for (String suggestion : mainPanel.getController().queryIngredientsName(searchText)) {
            ingredientComboBoxes.get(index).addItem(suggestion);
        }
    }
}