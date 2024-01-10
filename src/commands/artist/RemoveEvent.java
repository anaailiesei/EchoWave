package commands.artist;

import libraries.users.ArtistsLibrary;
import libraries.users.UsersLibrariesStats;
import entities.user.Artist;

public final class RemoveEvent {
    private static State state;

    private RemoveEvent() {
    }

    /**
     * Executes the add event command
     *
     * @param eventName The name of the event that should be added
     * @param username  The name of the entities.user that wants to add an event
     */
    public static void execute(final String eventName,
                               final String username) {
        Artist artist = ArtistsLibrary.getInstance().getArtistByName(username);
        checkConditions(eventName, username, artist);
        if (!state.equals(State.successfullyRemovedEvent)) {
            return;
        }
        artist.removeEvent(eventName);
    }

    /**
     * Check the conditions for performing this operation
     * For a successful operation the entities.user soul exist, and they should be an artist,
     * and the event should exist
     *
     * @param username The username for the entities.user that wants to add an album
     */
    private static void checkConditions(final String eventName,
                                        final String username,
                                        final Artist artist) {
        if (!UsersLibrariesStats.userExists(username)) {
            state = State.noUser;
        } else if (artist == null) {
            state = State.notArtist;
        } else if (!artist.eventExists(eventName)) {
            state = State.noEvent;
        } else {
            state = State.successfullyRemovedEvent;
        }
    }

    /**
     * Function for the output message of this command depending on the state variable
     *
     * @param username The username entities.user that adds an event
     * @return A string with the message
     */
    public static String toString(final String username) {
        return switch (state) {
            case noUser -> "The username " + username + " doesn't exist.";
            case notArtist -> username + " is not an artist.";
            case noEvent -> username + " doesn't have an event with the given name.";
            default -> username + " deleted the event successfully.";
        };
    }

    private enum State {
        noUser, successfullyRemovedEvent, notArtist, noEvent
    }
}
