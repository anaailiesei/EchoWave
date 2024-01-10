package entities.user;

import entities.Entity;
import fileio.input.UserInput;

import java.util.HashMap;

public abstract class User implements Entity {
    private UserInput userInput;

    public User(final UserInput userInput) {
        setUserInput(userInput);
    }

    protected User() {
    }

    /**
     * Sets the suer input for this user
     *
     * @param userInput the user input ot be set
     */

    public void setUserInput(final UserInput userInput) {
        this.userInput = userInput;
    }

    /**
     * Get the username
     *
     * @return the username
     */
    public String getName() {
        return userInput.getUsername();
    }

    /**
     * Sets the username
     *
     * @param username the username to be set
     */
    public void setUsername(final String username) {
        userInput.setUsername(username);
    }

    /**
     * Get the age of the user
     *
     * @return the age
     */
    public int getAge() {
        return userInput.getAge();
    }

    /**
     * Set the age of the user
     *
     * @param age The age to be set
     */
    public void setAge(final int age) {
        userInput.setAge(age);
    }

    /**
     * Get the city of the user
     *
     * @return The city
     */
    public String getCity() {
        return userInput.getCity();
    }

    /**
     * Set the city of the user
     *
     * @param city The city to be set
     */
    public void setCity(final String city) {
        userInput.setCity(city);
    }

    /**
     * Checks if the username starts with the given string
     *
     * @param searchString The string to be searched through the username
     * @return {@code true} if the username starts with the given string, {@code false} otherwise
     */
    public boolean usernameStartsWith(final String searchString) {
        return userInput.getUsername().startsWith(searchString);
    }

    /**
     * Checks if the user is deletable
     *
     * @return {@code true} if the user is deletable, {@code false} otherwise
     */
    public abstract boolean isDeletable();

    /**
     * Execute the wrapped command for the current user
     *
     * @return A map with he results of the wrapped command specific to each user
     */
    public abstract HashMap<String, Object> wrapped();

    /**
     * Checks if the user doesn't have any stats to show
     *
     * @return {@code true} if the user doesn't have stats to show, {@code false} otherwise
     */
    public boolean noStats() {
        return false;
    }
}
