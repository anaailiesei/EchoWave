package commands.admin;

import libraries.users.ArtistsLibrary;
import libraries.users.HostsLibrary;
import libraries.users.NormalUsersLibrary;
import libraries.users.UsersLibrariesStats;
import user.Artist;
import user.Host;
import user.NormalUser;
import user.UserType;

public final class AddUser {
    private static State state;

    private AddUser() {
    }

    /**
     * Execute the command for adding a user
     *
     * @param type     The type of the user that should be added
     * @param username The username of the user that should be added
     * @param age      The age of the user
     * @param city     The city of the user
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
     * The user shouldn't already exist for a successful operation.
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
     * @param username The username of the added user
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
