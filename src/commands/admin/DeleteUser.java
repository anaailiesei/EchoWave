package commands.admin;

import libraries.users.ArtistsLibrary;
import libraries.users.HostsLibrary;
import libraries.users.NormalUsersLibrary;
import libraries.users.UsersLibrariesStats;
import entities.user.Artist;
import entities.user.Host;
import entities.user.NormalUser;
import entities.user.User;

public final class DeleteUser {
    private static State state;

    private DeleteUser() {
    }

    /**
     * Execute the command for deleting a entities.user
     *
     * @param username The name of the entities.user that should be deleted
     */
    public static void execute(final String username) {
        checkConditions(username);
        if (!state.equals(State.successfullyDeletedUser)) {
            return;
        }
        if (ArtistsLibrary.getInstance().artistExists(username)) {
            Artist artist = ArtistsLibrary.getInstance().getArtistByName(username);
            ArtistsLibrary.getInstance().deleteArtist(artist);
        } else if (NormalUsersLibrary.getInstance().userExists(username)) {
            NormalUser user = NormalUsersLibrary.getInstance().getUserByName(username);
            assert user != null;
            NormalUsersLibrary.getInstance().deleteUser(user);
        } else if (HostsLibrary.getInstance().hostExists(username)) {
            Host host = HostsLibrary.getInstance().getHostByName(username);
            HostsLibrary.getInstance().deleteHost(host);
        }
    }

    /**
     * Check the conditions for performing this operation
     *
     * @param username The username for the entities.user that should be added
     */
    private static void checkConditions(final String username) {
        if (!UsersLibrariesStats.userExists(username)) {
            state = State.noUser;
        } else if (!deletePossible(username)) {
            state = State.notPossible;
        } else {
            state = State.successfullyDeletedUser;
        }
    }

    /**
     * Checks if the entities.user deletion is possible for the given username
     * The entities.user should be deletable for a successful operation
     *
     * @param username The entities.user's name we check for
     * @return {@code true} if the entities.user can be deleted, {@code false} otherwise
     */
    private static boolean deletePossible(final String username) {
        User user = UsersLibrariesStats.getUserByName(username);
        return user.isDeletable();
    }

    /**
     * Function for the output message of this command
     *
     * @param username The username of the deleted entities.user
     * @return A string with the message
     */
    public static String toString(final String username) {
        return switch (state) {
            case noUser -> "The username " + username + " doesn't exist.";
            case notPossible -> username + " can't be deleted.";
            default -> username + " was successfully deleted.";
        };
    }

    private enum State {
        noUser, successfullyDeletedUser, notPossible
    }
}
