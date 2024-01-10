package libraries.audio;

import entities.audio.collections.Album;
import libraries.GenericLibrary;
import libraries.users.ArtistsLibrary;
import entities.user.Artist;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    /**
     * Sort the albums in the order in which the artists were added in the artists library
     * This already makes a copy of the albums in the albums library so it doesn't change the
     * order of the albums in the actual library
     *
     * @return A list of the sorted albums
     * @see Album
     * @see Artist
     * @see ArtistsLibrary
     * @see AlbumsLibrary
     */
    public ArrayList<Album> sortAlbumsByArtistOrder() {
        ArrayList<Album> albums = new ArrayList<>(getItems());
        ArtistsLibrary artistsLibrary = ArtistsLibrary.getInstance();
        List<Artist> artists = artistsLibrary.getItems();

        Comparator<Album> albumComparator = Comparator.comparingInt(album -> {
            Artist artist = artistsLibrary.getArtistByName(album.getOwner());
            int index = artists.indexOf(artist);
            return index == -1 ? Integer.MAX_VALUE : index;
        });

        albums.sort(albumComparator);
        return albums;
    }
}
