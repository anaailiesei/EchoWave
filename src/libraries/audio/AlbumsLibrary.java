package libraries.audio;

import audio.collections.Album;
import libraries.GenericLibrary;

import java.util.ArrayList;

public final class AlbumsLibrary extends GenericLibrary<Album> {
    private static AlbumsLibrary instance = null;

    private AlbumsLibrary() {
    }

    /**
     * Gets the instance for the singleton class
     *
     * @return The instance
     */
    public static synchronized AlbumsLibrary getInstance() {
        if (instance == null) {
            instance = new AlbumsLibrary();
        }
        return instance;
    }

    /**
     * Adds an album from the library
     *
     * @param album The album to be added
     * @see Album
     */
    public void addAlbum(final Album album) {
        addItem(album);
    }

    /**
     * Removes an album from the library
     *
     * @param album The album to be removed
     */
    public void removeAlbum(final Album album) {
        removeItem(album);
    }

    /**
     * Gets an album from the library by its name
     *
     * @param albumName The name we search for
     * @return The album
     */
    public Album getAlbumByName(final String albumName) {
        ArrayList<Album> albums = getItems();
        return albums.stream().filter(album -> album.getName().equals(albumName))
                .findFirst()
                .orElse(null);
    }
}
