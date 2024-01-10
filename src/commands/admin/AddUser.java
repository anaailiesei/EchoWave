package commands.admin;

import libraries.users.ArtistsLibrary;
import libraries.users.HostsLibrary;
import libraries.users.NormalUsersLibrary;
import libraries.users.UsersLibrariesStats;
import entities.user.Artist;
import entities.user.Host;
import entities.user.NormalUser;
import entities.user.UserType;

public final class AddUser {
    private static State state;

    private AddUser() {
    }

    /**
     * Execute the command for adding a entities.user
     *
     * @param type     The type of the entities.user that should be added
     * @param username The username of the entities.user that should be added
     * @param age      The age of the entities.user
     * @param city     The city of the entities.user
     */
    public static void execute(final UserType type,
                               final String username,
                               final int age,
                               final String city) {
        checkConditions(username);
        if (state.equals(State.userExists)) {
            return;
        }
        if (type.equals(UserType.user)) {
            NormalUser user = new NormalUser(username, age, city);
            NormalUsersLibrary.getInstance().addItem(user);
        } else if (type.equals(UserType.artist)) {
            Artist artist = new Artist(username, age, city);
            ArtistsLibrary.getInstance().addItem(artist);
        } else {
            Host host = new Host(username, age, city);
            HostsLibrary.getInstance().addItem(host);
        }
    }

    /**
     * Check the conditions for performing this operation
     * The entities.user shouldn't already exist for a successful operation.
     *
     * @param username The username that should be added
     */
    private static void checkConditions(final String username) {
        if (UsersLibrariesStats.userExists(username)) {
            state = State.userExists;
        } else {
            state = State.successfullyAddedUser;
        }
    }

    /**
     * Function for the output message of this command
     *
     * @param username The username of the added entities.user
     * @return A string with the message
     */
    public static String toString(final String username) {
        if (state.equals(State.userExists)) {
            return "The username " + username + " is already taken.";
        }
        return "The username " + username + " has been added successfully.";
    }

    private enum State {
        userExists, successfullyAddedUser
    }
}
