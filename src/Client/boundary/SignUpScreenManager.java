package src.client.boundary;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import src.client.ClientMain;
import src.client.controller.UserController;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Class that manages the sign-up screen.
 */
public class SignUpScreenManager {
    @FXML
    private TextField usernameField; // Field for the username
    @FXML
    private PasswordField passwordField; // Field for the password
    private UserController userController = ClientMain.getUserController();
    private Stage stage; // The stage for the scene
    private Scene scene; // The scene of the GUI
    private Parent root; // The root node of the scene

    /**
     * Handles the register action when the register button is clicked.
     */
    @FXML
    public void handleRegisterAction() {
        signUp();
    }

    /**
     * Switches to the start screen when the back to start button is clicked.
     *
     * @param backToStartButtonEvent The event when the back to start button is clicked.
     */
    public void switchToStartScreen(javafx.event.ActionEvent backToStartButtonEvent) {
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource(
                    "src/Client/resources/fxml/StartScreen.fxml"));
            stage = (Stage) ((Node) backToStartButtonEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Signs up a user with the given username and password.
     * Checks if the username and password are valid.
     */
    public void signUp() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.isEmpty()) {
            JOptionPane.showMessageDialog(null, "You have to enter a username!");
            return;
        } else if (password == null || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "You have to enter a password!");
            return;
        }

        try {
            userController.createUser(username, password);

            if (userController.userExists(username)) {
                JOptionPane.showMessageDialog(null, "User registered successfully!");
                displayUserPassword(username);
            } else {
                JOptionPane.showMessageDialog(null, "User registration failed!");
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "That username already exists! Try another one!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Displays the password of the user with the given username.
     *
     * @param username The username of the user.
     */
    public void displayUserPassword(String username) {
        String password = userController.getUserPassword(username);
        System.out.println(username + ", " + password);
    }
}