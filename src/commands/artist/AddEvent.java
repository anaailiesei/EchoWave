package commands.artist;

import libraries.users.ArtistsLibrary;
import libraries.users.UsersLibrariesStats;
import profile.artist.DateValidation;
import profile.artist.Event;
import user.Artist;

public final class AddEvent {
    private static State state;

    private AddEvent() {
    }

    /**
     * Executes the add event action
     *
     * @param eventName   The name of the event to be added
     * @param username    The name of the artist that want to add the event
     * @param description The description fo the event
     * @param date        The dat eof the event
     */
    public static void execute(final String eventName,
                               final String username,
                               final String description,
                               final String date) {
        Artist artist = ArtistsLibrary.getInstance().getArtistByName(username);
        checkConditions(eventName, username, artist, date);
        if (!state.equals(State.successfullyAddedEvent)) {
            return;
        }
        Event event = new Event(eventName, description, date);
        artist.addEvent(event);
    }

    /**
     * Checks the conditions for performing this operation
     * For a successful operation, the user should exist, and they should be an artist,
     * the artist can't already have an event with the same name, and the date
     * of the event should be valid (between year 1900 and 2023, with a valid month and month day)
     *
     * @param eventName The name of the event
     * @param username  The user that want to add the event
     * @param artist    The artist (this should be non-null for a successful operation)
     * @param date      The dat eof the event
     */
    private static void checkConditions(final String eventName,
                                        final String username,
                                        final Artist artist,
                                        final String date) {
        if (!UsersLibrariesStats.userExists(username)) {
            state = State.noUser;
        } else if (artist == null) {
            state = State.notArtist;
        } else if (artist.eventExists(eventName)) {
            state = State.duplicatedEvent;
        } else if (!DateValidation.validateDate(date)) {
            state = State.invalidDate;
        } else {
            state = State.successfullyAddedEvent;
        }
    }

    /**
     * Function for the output message of this command depending on the state variable
     *
     * @param username The username user that adds the event
     * @return A string with the message
     */
    public static String toString(final String username) {
        return switch (state) {
            case noUser -> "The username " + username + " doesn't exist.";
            case successfullyAddedEvent -> username + " has added new event successfully.";
            case notArtist -> username + " is not an artist.";
            case duplicatedEvent -> username + " has another event with the same name.";
            case invalidDate -> "Event for " + username + " does not have a valid date.";
        };
    }

    private enum State {
        noUser, successfullyAddedEvent, notArtist, duplicatedEvent, invalidDate
    }
}
