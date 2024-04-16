package RecipeInputer;

import RecipeInputer.GUI.RecipeInputerMainFrame;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class RecipeInputerMain {

    private static Connection connection;

    public static void main(String[] args) {
        connect();
        new RecipeInputerController(connection);
    }


    private static void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://pgserver.mau.se:5432/drinkmaster3000", "ao7503", "t360bxdp");
            connection.setAutoCommit(false);
            System.out.println("Connection established");
        } catch (Exception e) {
            System.out.println("Error connecting controller to the database");
        }
    }
}
