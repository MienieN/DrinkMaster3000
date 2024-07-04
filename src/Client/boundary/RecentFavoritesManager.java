package src.client.boundary;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class RecentFavoritesManager {
    @FXML
    private Button Recent;
    @FXML
    private Button Favorites;
    @FXML
    private ListView recFavViewer;
    private Stage recFavStage;
    private Scene scene;
    private Parent root;
    private src.client.boundary.AlcDrinkScreenManager alcMan;
    private ObservableList<String> recentList;

    public void openRecFav(){
        try {
            recFavStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(
                    "src/client/resources/fxml/RecentFavorites.fxml"));
            loader.setController(this);
            root = loader.load();
            scene = new Scene(root);
            recFavStage.setScene(scene);
            recFavStage.setAlwaysOnTop(true);
            recFavStage.setResizable(false);
            recFavStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setAlcMan(src.client.boundary.AlcDrinkScreenManager alcMan){
        this.alcMan = alcMan;
        if(this.alcMan != null)
        {
            System.out.println("AlcManager set!");
        }
    }
    public void displayRecentList(){
        System.out.println("This should show a list: " + this.recentList);
        recFavViewer.setItems(recentList);
    }
    public void setRecent(ObservableList<String> sentList){
        recentList = FXCollections.observableArrayList();
        this.recentList = sentList;
        System.out.println(this.recentList);
    }
}

