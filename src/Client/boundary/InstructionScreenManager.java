package src.client.boundary;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.io.IOException;

public class InstructionScreenManager {
    // Stage for the help
    private Stage helpStage;
    // Scene for the help
    private Scene scene;
    // The root for the scene
    private Parent root;
    // ImageView for visual help
    @FXML
    private ImageView helpImageViewer;
    // Label for text help
    @FXML
    private Label helpLabel;
    // Image of instructions/help
    private Image helpImage = new Image(getClass().getClassLoader().getResourceAsStream(
            "src/Client/resources/helppictures/Introduction.png"));

    public void initialize() {
        helpLabel.setMaxWidth(180);
        helpLabel.setWrapText(true);
        helpLabel.setText("Welcome to the help screen for the alcoholic" +
                " beverages generator. With this you can find drink" +
                " recipes based on the ingredients you enter. Use the" +
                " buttons down below to navigate through the tutorial for" +
                " this function, or use the buttons above to get the tutorial" +
                " for the other functions!");
    }

    public void openHelpWindowAlc() {
        try {
            helpStage = new Stage();
            root = FXMLLoader.load(getClass().getClassLoader().getResource(
                    "src/Client/resources/fxml/HelpScreenAlc.fxml"));
            scene = new Scene(root);
            helpStage.setScene(scene);
            helpStage.setAlwaysOnTop(true);
            helpStage.setResizable(false);
            helpStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayIntroAlc() {
        helpImage = new Image(getClass().getClassLoader().getResourceAsStream(
                "src/Client/resources/helppictures/IntroductionAlcohol.png"));
        helpImageViewer.setImage(helpImage);
        helpLabel.setText("Welcome to the help screen for the alcoholic" +
                " beverages generator. With this you can find drink" +
                " recipes based on the ingredients you enter. Use the" +
                " buttons down below to navigate through the tutorial for" +
                " this function, or use the buttons above to get the tutorial" +
                " for the other functions!");
    }

    public void displayBaseHelpAlc() {
        helpImage = new Image(getClass().getClassLoader().getResourceAsStream(
                "src/Client/resources/helppictures/ChooseBaseAlcohol.png"));
        helpImageViewer.setImage(helpImage);
        helpLabel.setText("You start by selecting a base " +
                "alcohol for your drink in the " +
                "drop down menu in the marked area.");
    }

    public void displayIngredientHelpAlc() {
        helpImage = new Image(getClass().getClassLoader().getResourceAsStream(
                "src/Client/resources/helppictures/ChooseIngredientsAlcohol.png"));
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

    public void displayChooseDrinkHelpAlc() {
        helpImage = new Image(getClass().getClassLoader().getResourceAsStream(
                "src/Client/resources/helppictures/ChooseRecipeAlcohol.png"));
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

    public void displayUndoHelpAlc() {
        helpImage = new Image(getClass().getClassLoader().getResourceAsStream(
                "src/Client/resources/helppictures/RemoveIngredientsAlcohol.png"));
        helpImageViewer.setImage(helpImage);
        helpLabel.setText("If you wish to remove an ingredient you" +
                " have added from the list of added ingredients, you" +
                " can either press the Undo button as marked in the" +
                " picture to remove the last added ingredient," +
                " or you can select an ingredient from the marked list" +
                " and then press the Remove button.");
    }
}