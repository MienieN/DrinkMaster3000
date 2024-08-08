package src.client.controller;

import src.client.entity.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to manage user creation and retrieval.
 */
public class UserController {
    private Map<String, User> users = new HashMap<>(); // HashMap to store users

    /**
     * Create a new user with the given username and password.
     *
     * @param username Username of the user.
     * @param password Password of the user.
     * @return The created user.
     * @throws SQLException If the user already exists.
     */
    public User createUser(String username, String password) throws SQLException {
        if (users.containsKey(username)) {
            throw new IllegalArgumentException("User already exists");
        }

        User user = new User(username, password);
        users.put(username, user);
        return user;
    }

    /**
     * Get the user with the given username to check if the user already exists.
     *
     * @param username Username of the user.
     * @return The user with the given username.
     */
    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    /**
     * Get the password of the user with the given username.
     *
     * @param username Username of the user.
     * @return The password of the user.
     */
    public String getUserPassword(String username) {
        User user = users.get(username);
        if (user == null) {
            throw new IllegalArgumentException("User does not exist");
        }
        return user.getPassword();
    }
}