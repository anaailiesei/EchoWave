package commands.normalUser.general;

import libraries.users.NormalUsersLibrary;
import libraries.users.UsersLibrariesStats;
import managers.normalUser.AppManager;
import entities.user.NormalUser;

public final class SwitchConnectionStatus {
    private static State state;

    private SwitchConnectionStatus() {
    }

    /**
     * Execute the change page command
     *
     * @param username The name of the entities.user that wants to switchConnection
     */
    public static void execute(final String username) {
        checkConditions(username);
        if (!state.equals(State.switchedSuccessfully)) {
            return;
        }
        NormalUser user = NormalUsersLibrary.getInstance().getUserByName(username);
        assert user != null;
        AppManager app = user.getApp();
        app.toggleStatus();
    }

    /**
     * Checks the conditions for performing this operation
     * For a successful operation, the entities.user should exist adn they should be a regular entities.user
     *
     * @param username The name of the entities.user
     */
    private static void checkConditions(final String username) {
        if (!UsersLibrariesStats.userExists(username)) {
            state = State.noUser;
        } else if (!NormalUsersLibrary.getInstance().userExists(username)) {
            state = State.notNormalUser;
        } else {
            state = State.switchedSuccessfully;
        }
    }

    /**
     * Function for the output message of this command based on the state variable
     *
     * @param username the name of the entities.user that performs this command
     * @return A string with the message
     */
    public static String toString(final String username) {
        if (state.equals(State.noUser)) {
            return "The username " + username + " doesn't exist.";
        } else if (state.equals(State.notNormalUser)) {
            return username + " is not a normal entities.user.";
        }
        return username + " has changed status successfully.";
    }

    private enum State {
        noUser, notNormalUser, switchedSuccessfully
    }
}
