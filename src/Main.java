package src;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.Client.Controller.RecipeController;
import src.GUI.Boundary.DM3000Gui;
import src.GUI.Boundary.IMainFrame;
import src.GUI.Boundary.MainFrame;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    public static void main(String[] args){
        IMainFrame mainFrame = new MainFrame();
        RecipeController recipeController = new RecipeController(mainFrame);
        mainFrame.setRecipeController(recipeController);
        DM3000Gui dm = new DM3000Gui();
        launch(args);

    }

    @Override
    public void start(Stage stage) {
        try{
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("GUI.Boundary.DM3000Gui.fxml")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
