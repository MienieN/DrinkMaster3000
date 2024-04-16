package src.Client.Boundary.guiClasses;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIRecipeController extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage ViewRecipeStage) throws IOException {
        Parent viewRecipeScrollPane = FXMLLoader.load(getClass().getResource("/fxml/ViewRecipeScreen.fxml"));
        ViewRecipeStage.setTitle("DrinkMaster 3000");
        ViewRecipeStage.setScene(new Scene(viewRecipeScrollPane));
        ViewRecipeStage.show();
    }
}

