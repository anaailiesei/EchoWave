package statistics;

import entities.audio.Song;
import entities.audio.collections.Album;
import entities.user.NormalUser;
import libraries.audio.AlbumsLibrary;

import java.util.HashMap;
import java.util.LinkedHashMap;

public final class ListenTrackerArtist {
    private final ListenTracker albumsListenTracker = new ListenTracker();
    private final ListenTracker songsListenTracker = new ListenTracker();
    private final ListenTracker fansListenTracker = new ListenTracker();

    /**
     * Adds a listen for the specified album
     *
     * @param album The album for which we add a listen
     * @see Album
     */
    public void addListen(final Album album) {
        albumsListenTracker.addListen(album);
    }

    /**
     * Adds a listen for the specified song
     *
     * @param song The song for which we add a listen
     * @see Song
     */
    public void addListen(final Song song) {
        songsListenTracker.addListen(song);
    }

    /**
     * Adds a listen for the specified user
     *
     * @param user The user for which we add a listen
     * @see NormalUser
     */
    public void addListen(final NormalUser user) {
        fansListenTracker.addListen(user);
    }

    /**
     * Adds the specified number of listens for the specified album
     *
     * @param album The album for which we add a listen
     * @param count The number of listens that should be added
     * @see Album
     */
    public void addListen(final Album album, final int count) {
        albumsListenTracker.addListen(album, count);
    }

    /**
     * Adds the specified number of listens for the specified song
     *
     * @param song  The song for which we add a listen
     * @param count The number of listens that should be added
     * @see Song
     */
    public void addListen(final Song song, final int count) {
        songsListenTracker.addListen(song, count);
    }

    /**
     * Adds the specified number of listens for the specified entities.user
     *
     * @param user  The entities.user for which we add a listen
     * @param count The number of listens that should be added
     * @see NormalUser
     */
    public void addListen(final NormalUser user, final int count) {
        fansListenTracker.addListen(user, count);
    }

    /**
     * Get the top listens for each category (albums, songs, fans, listeners)
     * This is used with the wrapped command
     *
     * @return A map with the top results for each category
     */
    public HashMap<String, Object> topListensForEach() {
        HashMap<String, Object> result = new LinkedHashMap<>();

        result.put("topAlbums", albumsListenTracker.getTopFiveListens());
        result.put("topSongs", songsListenTracker.getTopFiveListens());
        result.put("topFans", fansListenTracker.getTopFiveListens().keySet().stream().toList());
        result.put("listeners", fansListenTracker.getSize());

        return result;
    }

    /**
     * Adds a listen for the following categories: Album, Song and User
     *
     * @param album The album for which we add a listen
     * @param song  The song for which we add a listen
     * @param user  The user for which we add a listen
     * @see Song
     * @see NormalUser
     * @see Album
     */
    public void addListenAll(final Album album, final Song song, final NormalUser user) {
        addListen(album);
        addListen(song);
        addListen(user);
    }

    /**
     * Adds a listen for the following categories: Album, Song and User
     *
     * @param albumName The name of the album for which we add a listen
     * @param song      The song for which we add a listen
     * @param user      The user for which we add a listen
     * @see Song
     * @see NormalUser
     */
    public void addListenAll(final String albumName, final Song song, final NormalUser user) {
        Album album = AlbumsLibrary.getInstance().getAlbumByName(albumName);
        albumsListenTracker.addListen(album);
        addListenAll(song, user);
    }

    /**
     * Adds a listen for the following categories: Song and User
     *
     * @param song The song for which we add a listen
     * @param user The user for which we add a listen
     * @see Song
     * @see NormalUser
     */
    public void addListenAll(final Song song, final NormalUser user) {
        addListen(song);
        addListen(user);
    }

    /**
     * Adds the specified number of listens for the following categories: Song and User
     *
     * @param song  The song for which we add a listen
     * @param user  The entities.user for which we add a listen
     * @param count The number of listens we add
     * @see Song
     * @see NormalUser
     */
    public void addListenAll(final Song song, final NormalUser user, final int count) {
        addListen(song, count);
        addListen(user, count);
    }
// TODO: Must check if he has made merch revenue

    /**
     * Check if the artist was listened
     *
     * @return {@code true} if the artist was listened, {@code false} otherwise
     */
    public boolean wasListened() {
        return (!albumsListenTracker.isEmpty()
                || !songsListenTracker.isEmpty()
                || !fansListenTracker.isEmpty());
    }
}
