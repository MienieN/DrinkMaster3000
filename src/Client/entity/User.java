package src.client.entity;

/**
 * Class to represent a user.
 */
public class User {
    private String username; // Username of the user
    private String password; // Password of the user

    /**
     * Create a new user with the given username and password.
     *
     * @param username Username of the user.
     * @param password Password of the user.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Get the password of the user.
     *
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }
}