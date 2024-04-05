package RecipeInputer;

import RecipeInputer.GUI.RecipeInputerMainFrame;

import javax.sound.midi.Soundbank;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class RecipeInputer {

    private static Connection connection;
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args){
        new RecipeInputerMainFrame(700, 500);
        connect();
        int choice;

        do{
            System.out.println("1: enter recipe");
            System.out.println("0: Exit");
            choice = Integer.parseInt(scanner.nextLine());
            switch (choice){
                case 1:
                    addRecipe();
                    break;
                case 0:
                    System.exit(0);
            }
        }while(choice != 0);

    }

    private static void addRecipe() {
        String recipename;
        String ingredientName = "hej";
        ArrayList<String> ingredients = new ArrayList<>();
        String instructions;

        System.out.println("Enter recipe name");
        recipename = scanner.nextLine();
        while (true){
            System.out.println("Enter ingredient, enter e to exit, enter d to discard");
            ingredientName = scanner.nextLine();
            if (ingredientName.equals("e")) {
                break;
            } else if (ingredientName.equals("d")) {
                recipename = null;
                ingredientName = null;
                ingredients.clear();
            }else{
                ingredients.add(ingredientName);
            }
        }

        Savepoint savepoint = null;
        try {
            savepoint = connection.setSavepoint();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql = "INSERT INTO recipes values (?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, recipename);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected != 1) {
                System.out.println("Error: Failed to insert recipe");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String sql2 = "INSERT INTO ingredients values (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql2)) {
            // Loop through the array and insert each element individually
            for (String ingredient : ingredients) {
                // Set the array element as a parameter in the prepared statement
                statement.setString(1, ingredient);
                statement.setBoolean(2, false);

                // Execute the SQL INSERT statement for each array element
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected != 1) {
                    System.err.println("Error: Failed to insert ingredient");
                    // Rollback to the savepoint
                    connection.rollback(savepoint);
                }
            }

            // Commit the transaction if all inserts were successful
            connection.commit();
            System.out.println("Array elements inserted successfully.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void connect(){
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://pgserver.mau.se:5432/drinkmaster3000", "ao7503", "t360bxdp");
            connection.setAutoCommit(false);
            System.out.println("Connection established");
        } catch (Exception e) {
            System.out.println("Error connecting controller to the database");
        }
    }
}
