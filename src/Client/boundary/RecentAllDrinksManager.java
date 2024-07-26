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

public class RecentAllDrinksManager {
    // Button for viewing the Recent list
    @FXML
    private Button Recent;
    // Button for viewing all drinks
    @FXML
    private Button All;
    // listView which will show the lists
    @FXML
    private ListView<String> recFavViewer;
    // Stage for the Recent/All drinks
    private Stage recFavStage;
    // Scene for Recent/All Drinks
    private Scene scene;
    // parent for Recent/All Drinks
    private Parent root;
    // RecipeController
    private src.client.controller.RecipeController recController;
    // List that holds the recent drinks
    private ObservableList<String> recentList = FXCollections.observableArrayList();
    // List that holds all the drinks available
    private ObservableList<String> allList;

    /**
     * Opens the window which holds the list for showing recent and all drinks
     */
    public void openRecFav(){
        try {
            recFavStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(
                    "src/client/resources/fxml/RecentFavorites.fxml"));
            loader.setController(this);
            root = loader.load();
            scene = new Scene(root);
            recFavStage.setScene(scene);
            recFavStage.setAlwaysOnTop(false);
            recFavStage.setResizable(false);
            recFavStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Puts the recents list in the viewer for showing
     */
    public void displayRecentList(){
        System.out.println("This should show a list: " + this.recentList);
        recFavViewer.setItems(recentList);
    }

    /**
     * Puts the all drinks list in the viewer for showing
     */
    public void displayAllList(){
        recFavViewer.setItems(allList);
    }

    /**
     * Sets the all drinks list
     * @param sentList a list of all drinks fetched from the RecipeController
     */
    public void setAllList(ObservableList<String> sentList){
        allList = FXCollections.observableArrayList();
        this.allList = sentList;
        System.out.println(this.allList);
    }

    /**
     * Sets the RecipeController
     * @param recCon the RecipeController
     */
    public void setRecController(src.client.controller.RecipeController recCon){
        this.recController = recCon;
    }

    /**
     * Gets the chosen value from the viewer
     * @return The chosen value from the viewer
     */
    public String getRecipeName(){
            return recFavViewer.getSelectionModel().getSelectedItem();
    }

    /**
     * Gets the recipe instructions for showing.
     */
    public void getRecipeInstructions(){
        String selRecipe = getRecipeName();
        System.out.println(selRecipe);
        if(selRecipe != null && selRecipe != ""){
            recController.getRecipeInstructionsForChosenRecipe(selRecipe);
        }
        else{
            System.out.println("Error with selection!");
        }
    }

    /**
     * Adds an item to the recents list
     * @param addToList The item that should be added to the recents list
     */
    public void addRecList(String addToList){
        recentList.add(addToList);
    }
}

