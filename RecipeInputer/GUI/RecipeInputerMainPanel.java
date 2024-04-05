package RecipeInputer.GUI;

import src.GUI.Boundary.MainFrame;

import javax.swing.*;

public class RecipeInputerMainPanel extends JPanel {
    private RecipeInputerMainFrame mainFrame;
    private JCheckBox checkBox;
    private  JTextField recipeNameTextField;

    public RecipeInputerMainPanel(RecipeInputerMainFrame mainFrame, int width, int height) {
        this.mainFrame = mainFrame;
        setSize(width, height);
        checkBox = new JCheckBox("Alcoholic?");
        add(checkBox);
        recipeNameTextField = new JTextField("Recipe Name:");
        add(recipeNameTextField);
        JTextField textField = new JTextField();
        add(textField);
    }
}
