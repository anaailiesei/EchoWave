package commands.artist;

import entities.audio.Song;
import entities.audio.collections.Album;
import libraries.audio.AlbumsLibrary;
import libraries.audio.SongsLibrary;
import libraries.users.ArtistsLibrary;
import libraries.users.UsersLibrariesStats;
import entities.user.Artist;

import java.util.ArrayList;

public final class AddAlbum {
    private static State state;

    private AddAlbum() {
    }

    /**
     * Executes the add album command
     *
     * @param albumName   The name of the album that should be added
     * @param username    The name of the entities.user that wants to add an album
     * @param releaseYear The release year of the album
     * @param description The description of the album
     * @param songs       The list of songs
     */
    public static void execute(final String albumName,
                               final String username,
                               final int releaseYear,
                               final String description,
                               final ArrayList<Song> songs) {
        Artist artist = ArtistsLibrary.getInstance().getArtistByName(username);
        checkConditions(albumName, username, artist, songs);
        if (!state.equals(State.successfullyAddedAlbum)) {
            return;
        }
        Album album = new Album(albumName, username, releaseYear, description, songs);
        artist.addAlbum(album);
        SongsLibrary.getInstance().addSongsFromAlbum(album);
        AlbumsLibrary.getInstance().addAlbum(album);
    }

    /**
     * Check the conditions for performing this operation
     * For a successful operation the entities.user should exist and should be an artist,
     * the artist shouldn't have an album with the same name and the album added
     * shouldn't have duplicated songs
     *
     * @param username  The username for the entities.user that wants to add an album
     * @param albumName The name of the album to be checked
     */
    private static void checkConditions(final String albumName,
                                        final String username,
                                        final Artist artist,
                                        final ArrayList<Song> songs) {
        if (!UsersLibrariesStats.userExists(username)) {
            state = State.noUser;
        } else if (artist == null) {
            state = State.notArtist;
        } else if (artist.albumExists(albumName)) {
            state = State.duplicatedAlbum;
        } else if (hasDuplicates(songs)) {
            state = State.repeatedSong;
        } else {
            state = State.successfullyAddedAlbum;
        }
    }

    /**
     * Checks if a list of given songs has duplicates
     *
     * @param songs The lis of songs to be checked
     * @return {@code true} if the list contains duplicates, {@code false} otherwise
     */
    private static boolean hasDuplicates(final ArrayList<Song> songs) {
        long distinctCount = songs.stream().map(Song::getName).distinct().count();
        return distinctCount < songs.size();
    }

    /**
     * Function for the output message of this command depending on the state variable
     *
     * @param username The username entities.user that adds an album
     * @return A string with the message
     */
    public static String toString(final String username) {
        return switch (state) {
            case noUser -> "The username " + username + " doesn't exist.";
            case notArtist -> username + " is not an artist.";
            case duplicatedAlbum -> username + " has another album with the same name.";
            case repeatedSong -> username + " has the same song at least twice in this album.";
            default -> username + " has added new album successfully.";
        };
    }

    private enum State {
        noUser, successfullyAddedAlbum, notArtist, repeatedSong, duplicatedAlbum
    }
}
