package addRecipe.gui;

import addRecipe.AddRecipeController;


import javax.swing.*;
import java.util.HashMap;

/**
 * The AddRecipeMainFrame class represents the main frame of the application.
 * This frame contains the user interface elements for adding recipes and interacting with the application.
 * This class is designed for the application owners or development team members, not the users.
 */
public class AddRecipeMainFrame extends JFrame {
    private JPanel mainPanel;                   // Main panel of the frame
    private AddRecipeController controller;     // The controller managing interactions with the recipe database

    /**
     * Constructs a new AddRecipeMainFrame with the specified width, height, and controller.
     *
     * @param width      The width of the main frame.
     * @param height     The height of the main frame.
     * @param controller The AddRecipeController instance responsible for managing interactions with the recipe database.
     */
    public AddRecipeMainFrame(int width, int height, AddRecipeController controller){
        this.controller = controller;
        mainPanel = new AddRecipeMainPanel(this, width, height);
        setSize(width, height);
        add(mainPanel);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Adds a new recipe to the database with the specified name, ingredients, and instructions.
     *
     * @param name         The name of the recipe.
     * @param ingredients  A HashMap containing the ingredients of the recipe and their availability status.
     * @param instructions The instructions for preparing the recipe.
     */
    public void addRecipeToDatabase(String name, HashMap<String, Boolean> ingredients, String instructions) {
        controller.addRecipe(name, ingredients, instructions);
    }
}