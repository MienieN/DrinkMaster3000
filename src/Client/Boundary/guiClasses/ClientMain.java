package src.Client.Boundary.guiClasses;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientMain extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/StartScreen.fxml"));
        Scene startScene = new Scene(root);
        primaryStage.setScene(startScene);
        primaryStage.setTitle("DrinkMaster 3000");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}