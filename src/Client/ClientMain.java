package src.Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.Client.Controller.IngredientsController;

import java.io.IOException;

public class ClientMain extends Application {
    private static IngredientsController ingredientsController;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("resources/fxml/StartScreen.fxml"));
        Scene startScene = new Scene(root);
        primaryStage.setScene(startScene);
        primaryStage.setTitle("DrinkMaster 3000");
        primaryStage.show();
    }

    public static void main(String[] args) {
        ingredientsController = new IngredientsController();
        launch();
    }

    public static IngredientsController getIngredientsController(){
        return ingredientsController;
    }
}