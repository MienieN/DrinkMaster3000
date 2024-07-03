package src.client.controller;

import src.client.entity.User;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserController {
    private Map<String, User> users = new HashMap<>();
    private Connection connection;
    private DatabaseMetaData DatabaseConnection;

    public User createUser(String username, String password) throws SQLException {
        if (users.containsKey(username)) {
            throw new IllegalArgumentException("User already exists");
        }

        String sql = "INSERT INTO login (username, password) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        User user = new User(username, password);
        users.put(username, user);
        return user;
    }
}
