package libraries.users;

import audio.collections.Album;
import libraries.GenericLibrary;
import libraries.audio.AlbumsLibrary;
import libraries.audio.SongsLibrary;
import user.Artist;

import java.util.ArrayList;
import java.util.HashSet;

public final class ArtistsLibrary extends GenericLibrary<Artist> {
    private static ArtistsLibrary instance = null;

    private ArtistsLibrary() {
    }

    /**
     * Gets the instance for the artists' library class (singleton pattern)
     * Initialize it if needed
     *
     * @return The library instance
     */
    public static synchronized ArtistsLibrary getInstance() {
        if (instance == null) {
            instance = new ArtistsLibrary();
        }
        return instance;
    }

    /**
     * Check if the artist exists given their name
     *
     * @param username The username of the searched artist
     * @return {@code true} if the artist exists in the library, {@code false} otherwise
     */
    public boolean artistExists(final String username) {
        ArrayList<Artist> artists = getItems();
        if (artists == null || artists.isEmpty()) {
            return false;
        }
        for (Artist artist : artists) {
            if (artist.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets an artist by their username
     *
     * @param username The username we search for
     * @return The found artist
     */
    public Artist getArtistByName(final String username) {
        if (username == null) {
            return null;
        }
        ArrayList<Artist> artists = getItems();
        if (artists == null || artists.isEmpty()) {
            return null;
        }
        for (Artist artist : artists) {
            if (artist.getUsername().equals(username)) {
                return artist;
            }
        }
        return null;
    }

    /**
     * Deletes the given artist from the Artists library
     * It also removes their albums and songs from the album and songs libraries
     *
     * @param artist The artist to delete
     */
    public void deleteArtist(final Artist artist) {
        HashSet<Album> albums = artist.getAlbums();
        for (Album album : albums) {
            album.removeAllLikes();
            SongsLibrary.getInstance().removeSongsFromAlbum(album);
            AlbumsLibrary.getInstance().removeAlbum(album);
        }
        removeItem(artist);
    }
}
