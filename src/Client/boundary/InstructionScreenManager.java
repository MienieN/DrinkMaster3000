package src.Client.boundary;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;

public class InstructionScreenManager {

    Parent root;
    Stage helpStage;
    Scene scene;
    @FXML
    ImageView helpImageViewer;
    Image helpImage1 = new Image(getClass().getClassLoader().getResourceAsStream("src/Client/resources/helppictures/EasterEgg.png"));
    Image helpImage2 = new Image(getClass().getClassLoader().getResourceAsStream("src/Client/resources/helppictures/EasterEgg2.png"));
    Image helpImage3 = new Image(getClass().getClassLoader().getResourceAsStream("src/Client/resources/helppictures/EasterEgg3.png"));
    public void openHelpWindow() {
        System.out.println(getClass().getClassLoader().getResource("src/Client/resources/fxml/HelpScreen.fxml"));
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
    public void displayImage1() {
        helpImageViewer.setImage(helpImage1);
    }
    public void displayImage2() {
        helpImageViewer.setImage(helpImage2);
    }
    public void displayImage3() {
        helpImageViewer.setImage(helpImage3);
    }
}
