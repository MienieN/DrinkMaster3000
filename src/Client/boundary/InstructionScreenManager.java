package src.client.boundary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.io.IOException;
import java.util.Objects;

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
        helpLabel.setText("Welcome to the help screen! Use" +
                " the buttons down below to navigate the" +
                " tutorial for this particular function, or" +
                " use the buttons above to see the tutorial" +
                " for any chosen function!");
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
                "chosen. To view a recipe, double click on" +
                " a drink name in the area marked in the picture");
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

    public void openHelpWindowNonAlc() {
        try {
            helpStage = new Stage();
            root = FXMLLoader.load(getClass().getClassLoader().getResource(
                    "src/Client/resources/fxml/HelpScreenNonAlc.fxml"));
            scene = new Scene(root);
            helpStage.setScene(scene);
            helpStage.setAlwaysOnTop(true);
            helpStage.setResizable(false);
            helpStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayIntroNonAlc() {
        helpImage = new Image(getClass().getClassLoader().getResourceAsStream(
                "src/Client/resources/helppictures/IntroductionNonAlc.png"));
        helpImageViewer.setImage(helpImage);
        helpLabel.setText("Welcome to the help screen for the non-alcoholic" +
                " beverages generator. With this you can find drink" +
                " recipes based on the ingredients you enter. Use the" +
                " buttons down below to navigate through the tutorial for" +
                " this function, or use the buttons above to get the tutorial" +
                " for the other functions!");
    }

    public void displayIngredientHelpNonAlc() {
        helpImage = new Image(getClass().getClassLoader().getResourceAsStream(
                "src/Client/resources/helppictures/ChooseIngredientNonAlc.png"));
        helpImageViewer.setImage(helpImage);
        helpLabel.setText("ingredients will " +
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

    public void displayUndoHelpNonAlc() {
        helpImage = new Image(getClass().getClassLoader().getResourceAsStream(
                "src/Client/resources/helppictures/RemoveIngredientsNonAlc.png"));
        helpImageViewer.setImage(helpImage);
        helpLabel.setText("If you wish to remove an ingredient you" +
                " have added from the list of added ingredients, you" +
                " can either press the Undo button as marked in the" +
                " picture to remove the last added ingredient," +
                " or you can select an ingredient from the marked list" +
                " and then press the Remove button.");
    }

    public void displayChooseDrinkHelpNonAlc() {
        helpImage = new Image(getClass().getClassLoader().getResourceAsStream(
                "src/Client/resources/helppictures/ChooseRecipeNonAlc.png"));
        helpImageViewer.setImage(helpImage);
        helpLabel.setText("After you have chosen some ingredients, " +
                "the marked list on the right side " +
                "will start to show you recipe names " +
                "based on the ingredients you have " +
                "chosen. To view the recipe for a drink," +
                " double click the name in the drink list" +
                " marked in the picture.");
    }

    public void openHelpWindowDisc() {
        try {
            helpStage = new Stage();
            root = FXMLLoader.load(getClass().getClassLoader().getResource(
                    "src/Client/resources/fxml/HelpScreenDiscover.fxml"));
            scene = new Scene(root);
            helpStage.setScene(scene);
            helpStage.setAlwaysOnTop(true);
            helpStage.setResizable(false);
            helpStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayIntroDisc() {
        helpImage = new Image(getClass().getClassLoader().getResourceAsStream(
                "src/Client/resources/helppictures/IntroductionDiscover.png"));
        helpImageViewer.setImage(helpImage);
        helpLabel.setText("Welcome to the help screen for the Discover screen" +
                " beverages generator. With this you can find drink" +
                " recipes based on the ingredients you enter. Use the" +
                " buttons down below to navigate through the tutorial for" +
                " this function, or use the buttons above to get the tutorial" +
                " for the other functions!");
    }

    public void displayIngredientHelpDisc() {
        helpImage = new Image(getClass().getClassLoader().getResourceAsStream(
                "src/Client/resources/helppictures/ChooseIngredientsDiscover.png"));
        helpImageViewer.setImage(helpImage);
        helpLabel.setText("ingredients will " +
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

    public void displayUndoHelpDisc() {
        helpImage = new Image(getClass().getClassLoader().getResourceAsStream(
                "src/Client/resources/helppictures/RemoveIngredientsDiscover.png"));
        helpImageViewer.setImage(helpImage);
        helpLabel.setText("If you wish to remove an ingredient you" +
                " have added from the list of added ingredients, you" +
                " can either press the Undo button as marked in the" +
                " picture to remove the last added ingredient," +
                " or you can select an ingredient from the marked list" +
                " and then press the Remove button.");
    }

    public void displayChooseDrinkHelpDisc() {
        helpImage = new Image(getClass().getClassLoader().getResourceAsStream(
                "src/Client/resources/helppictures/ChooseRecipeDiscover.png"));
        helpImageViewer.setImage(helpImage);
        helpLabel.setText("After you have chosen some ingredients, " +
                "the marked list on the right side " +
                "will start to show you recipe names " +
                "based on the ingredients you have " +
                "chosen. To view the recipe for a drink," +
                " double click the name in the drink list" +
                " marked in the picture.");
    }

    public void openHelpWindowStart() {
        try {
            helpStage = new Stage();
            root = FXMLLoader.load(getClass().getClassLoader().getResource(
                    "src/Client/resources/fxml/HelpScreenStart.fxml"));
            scene = new Scene(root);
            helpStage.setScene(scene);
            helpStage.setAlwaysOnTop(true);
            helpStage.setResizable(false);
            helpStage.show();
            helpLabel.setText("Welcome to the help screen! What you see" +
                    " in the picture is the start screen which have some" +
                    " options. Discover New Drinks will find you recipes" +
                    " for drinks with no alcoholic version. Non Alcoholic" +
                    " will let you find non alcoholic versions of drinks" +
                    " that would otherwise contain alcohol. Alcoholic will" +
                    " let you find recipes for drinks containing alcohol. " +
                    "Press the buttons above to find the tutorial for the" +
                    " different functions.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}