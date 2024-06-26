package src.client.boundary;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Class that manages the GUI for the instruction screen.
 */
public class InstructionScreenManager {
    private Parent root;
    private Stage helpStage;
    private Scene scene;
    @FXML
    private ImageView helpImageViewer;
    @FXML
    private Label helpLabel;
    Image helpImage = new Image(getClass().getClassLoader().getResourceAsStream("src/Client/resources/helppictures/Introduction.png"));

    /**
     * Initializes the instruction screen.
     */
    public void initialize() {
        helpLabel.setMaxWidth(160);
        helpLabel.setWrapText(true);
        helpLabel.setText("Hello and welcome to DrinkMaster3000! " +
                "This app is made for those who " +
                "want to make a drink, with or " +
                "without alcohol, with whatever " +
                "ingredients you might have at " +
                "home but dont know a good recipe " +
                "for. Use the buttons down below " +
                "to navigate through this tutorial.");
    }

    /**
     * Opens the help window.
     */
    public void openHelpWindow() {
        try {
            helpStage = new Stage();
            root = FXMLLoader.load(getClass().getClassLoader().getResource("src/Client/resources/fxml/HelpScreen.fxml"));
            scene = new Scene(root);
            helpStage.setScene(scene);
            helpStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the introduction help text and image.
     */
    public void displayIntro() {
        helpImage = new Image(getClass().getClassLoader().getResourceAsStream("src/Client/resources/helppictures/Introduction.png"));
        helpImageViewer.setImage(helpImage);
        helpLabel.setText("Hello and welcome to DrinkMaster3000! " +
                "This app is made for those who " +
                "want to make a drink, with or " +
                "without alcohol, with whatever " +
                "ingredients you might have at " +
                "home but dont know a good recipe " +
                "for. Use the buttons down below " +
                "to navigate through this tutorial.");
    }

    /**
     * Displays the base drink help text and image.
     */
    public void displayBaseHelp() {
        helpImage = new Image(getClass().getClassLoader().getResourceAsStream("src/Client/resources/helppictures/BaseDrink.png"));
        helpImageViewer.setImage(helpImage);
        helpLabel.setText("You start by selecting a base " +
                "alcohol for your drink in the " +
                "drop down menu in the marked area.");
    }

    /**
     * Displays the ingredient help text and image.
     */
    public void displayIngredientHelp() {
        helpImage = new Image(getClass().getClassLoader().getResourceAsStream("src/Client/resources/helppictures/IngredientHelp.png"));
        helpImageViewer.setImage(helpImage);
        helpLabel.setText("After you have chosen your " +
                "base alcohol, ingredients will " +
                "show up at the 4 middle buttons " +
                "marked by in the window. Here you can " +
                "choose between the options presented " +
                "based on what you have at home." +
                " If you dont have any of the " +
                "presented ingredients, you can " +
                "press the None of the above " +
                "button in the smaller marked " +
                "area. Pressing the None of the " +
                "above button will present you " +
                "with new options to choose from.");
    }

    /**
     * Displays the choose drink help text and image.
     */
    public void displayChooseDrinkHelp() {
        helpImage = new Image(getClass().getClassLoader().getResourceAsStream("src/Client/resources/helppictures/ChooseDrink.png"));
        helpImageViewer.setImage(helpImage);
        helpLabel.setText("After you have chosen some ingredients, " +
                "the marked list on the right side " +
                "will start to show you recipe names " +
                "based on the ingredients you have " +
                "chosen. To view the recipe for a drink," +
                " select it in the menu and click the" +
                " View Recipe button. This will open a" +
                " new window with the recipe and instructions.");
    }
}