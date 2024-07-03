package src.client.boundary;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import src.client.controller.UserController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class SignUpScreenManager {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button registerButton;
    private UserController userController = new UserController();
    private Connection connection;

    @FXML
    public void handleRegisterAction() {
        signUp();
    }

    public void switchToSignInScreen() {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(
                    "src/Client/resources/fxml/SignInScreen.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void signUp() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            userController.createUser(username, password);
            switchToSignInScreen();
        } catch (IllegalArgumentException e) {
            // Handle the case where the user already exists
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
