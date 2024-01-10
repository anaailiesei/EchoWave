package commands.artist;

import libraries.users.ArtistsLibrary;
import libraries.users.UsersLibrariesStats;
import profile.artist.Merch;
import user.Artist;

public final class AddMerch {
    private static State state;

    private AddMerch() {
    }

    /**
     * Executes the add merch operation
     *
     * @param merchName   The name of the merch to be added
     * @param username    The name of the user that wants to add the merch
     * @param description The description of the merch
     * @param price       The price of the merch
     */
    public static void execute(final String merchName,
                               final String username,
                               final String description,
                               final int price) {
        Artist artist = ArtistsLibrary.getInstance().getArtistByName(username);
        checkConditions(merchName, username, artist, price);
        if (!state.equals(State.successfullyAddedMerch)) {
            return;
        }
        Merch merch = new Merch(merchName, price, description);
        artist.addMerch(merch);
    }

    /**
     * Checks the conditions for performing this operation
     * The merch can be successfully added if the given user exists,
     * it's an artist and there is no other merch with the given name.
     * The price of the merch should also be positive.
     *
     * @param merchName The name of the merch to be added
     * @param username  The name of the user that perform the operation
     * @param artist    The artist that wants to add the merch (this should be a non-null
     *                  reference for a successful operation)
     * @param price     The price of the merch (should be greater than 0 for a successful operation)
     */
    private static void checkConditions(final String merchName,
                                        final String username,
                                        final Artist artist,
                                        final int price) {
        if (!UsersLibrariesStats.userExists(username)) {
            state = State.noUser;
        } else if (artist == null) {
            state = State.notArtist;
        } else if (artist.merchExists(merchName)) {
            state = State.duplicatedMerch;
        } else if (price < 0) {
            state = State.invalidPrice;
        } else {
            state = State.successfullyAddedMerch;
        }
    }

    /**
     * Function for the output message of this command depending on the state variable
     *
     * @param username The username user that adds the merch
     * @return A string with the message
     */
    public static String toString(final String username) {
        return switch (state) {
            case noUser -> "The username " + username + " doesn't exist.";
            case successfullyAddedMerch -> username + " has added new merchandise successfully.";
            case notArtist -> username + " is not an artist.";
            case duplicatedMerch -> username + " has merchandise with the same name.";
            case invalidPrice -> "Price for merchandise can not be negative.";
        };
    }

    private enum State {
        noUser, successfullyAddedMerch, notArtist, duplicatedMerch, invalidPrice
    }
}
