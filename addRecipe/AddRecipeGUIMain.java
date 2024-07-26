package addRecipe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AddRecipeGUIMain extends Application {
    private static Connection connection;

    public static void main(String[] args) {
        connect();
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        AddRecipeController controller = new AddRecipeController(connection);
        //mainpane
        //Group root = new Group();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("AddRecipeGUI.fxml")));

        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin window - Add Recipe");
        primaryStage.show();
    }
    
    private static void connect() {
        try {
            // Establish connection to the PostgreSQL database
            connection = DriverManager.getConnection("jdbc:postgresql://pgserver.mau.se:5432/drinkmaster3000",
                    "ao7503", "t360bxdp");
            System.out.println("Connection established");
        } catch (SQLException e) {
            System.out.println("Error in connection");
            throw new RuntimeException(e);
        }
    }
    
}
