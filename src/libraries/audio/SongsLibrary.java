package libraries.audio;

import entities.audio.Song;
import entities.audio.collections.Album;
import libraries.GenericLibrary;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;

public final class SongsLibrary extends GenericLibrary<Song> {
    private static SongsLibrary instance = null;
    @Getter
    private static int adDuration;

    private SongsLibrary() {
    }

    /**
     * Gets the instance for the songs' library class (singleton pattern)
     * Initialize it if needed
     *
     * @return The library instance
     */
    public static synchronized SongsLibrary getInstance() {
        if (instance == null) {
            instance = new SongsLibrary();
        }
        return instance;
    }

    /**
     * Gets the top songs by likes and keeps only the specified number of songs
     *
     * @param maxNumber The maximum number of songs to keep
     * @return The list of songs with the most likes
     */
    public ArrayList<Song> getTopSongs(final int maxNumber) {
        if (getItems() == null) {
            return null;
        }
        ArrayList<Song> songs = new ArrayList<>(getItems());
        songs.sort(Comparator.comparing(Song::getLikes).reversed());
        if (songs.size() > maxNumber) {
            songs.retainAll(songs.subList(0, maxNumber));
        }
        return songs;
    }

    /**
     * Adds the songs from the specified album to the library
     *
     * @param album The album from which we add the songs to the library
     */
    public void addSongsFromAlbum(final Album album) {
        for (Song song : album.getCollection()) {
            addItem(song);
        }
    }

    /**
     * Removes the songs that are part of the specified album from the library
     *
     * @param album The album from which we get the songs to delete
     */
    public void removeSongsFromAlbum(final Album album) {
        for (Song song : album.getCollection()) {
            removeItem(song);
        }
    }

    public static void setAdDuration(int adDuration) {
        SongsLibrary.adDuration = adDuration;
    }
}
