package commands.artist;

import audio.collections.Album;
import libraries.users.ArtistsLibrary;
import libraries.users.UsersLibrariesStats;
import user.Artist;

public final class RemoveAlbum {
    private static State state;

    private RemoveAlbum() {
    }

    /**
     * Executes the remove album command
     *
     * @param albumName The name of the album that should be added
     * @param username  The name of the user that wants to add an album
     */
    public static void execute(final String albumName,
                               final String username) {
        Artist artist = ArtistsLibrary.getInstance().getArtistByName(username);
        checkConditions(albumName, username, artist);
        if (!state.equals(State.successfullyRemoveAlbum)) {
            return;
        }
        Album album = artist.getAlbumByName(albumName);
        artist.removeAlbum(album);
    }

    /**
     * Check the conditions for performing this operation
     * For a successful operation, the user should exist, and they should be an artist,
     * the album should exist, and it should be deletable (no user has this album or any song
     * from it playing or added to a playlist)
     *
     * @param username The username for the user that wants to remove an album
     */
    private static void checkConditions(final String albumName,
                                        final String username,
                                        final Artist artist) {
        if (!UsersLibrariesStats.userExists(username)) {
            state = State.noUser;
        } else if (artist == null) {
            state = State.notArtist;
        } else if (!artist.albumExists(albumName)) {
            state = State.noAlbum;
        } else if (!artist.getAlbumByName(albumName).isDeletable()) {
            state = State.notDeletable;
        } else {
            state = State.successfullyRemoveAlbum;
        }
    }

    /**
     * Function for the output message of this command
     *
     * @param username The username user that removes an album
     * @return A string with the message
     */
    public static String toString(final String username) {
        return switch (state) {
            case noUser -> "The username " + username + " doesn't exist.";
            case notArtist -> username + " is not an artist.";
            case noAlbum -> username + " doesn't have an album with the given name.";
            case notDeletable -> username + " can't delete this album.";
            default -> username + " deleted the album successfully.";
        };
    }

    private enum State {
        noUser, successfullyRemoveAlbum, notArtist, noAlbum, notDeletable
    }
}
