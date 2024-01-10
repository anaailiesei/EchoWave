package statistics;

import audio.Audio;
import audio.Episode;
import audio.Song;
import audio.collections.Album;

import java.util.HashMap;
import java.util.LinkedHashMap;

public final class ListenTrackerNormalUser {
    private final ListenTracker artistsListenTracker = new ListenTracker();
    private final ListenTracker albumsListenTracker = new ListenTracker();
    private final ListenTracker songsListenTracker = new ListenTracker();
    private final ListenTracker episodesListenTracker = new ListenTracker();
    private final ListenTracker genresListenTracker = new ListenTracker();

    /**
     * Adds a listen or the specified album
     *
     * @param album The album for which we add a listen
     * @see Album
     */
    public void addListen(final Album album) {
        albumsListenTracker.addListen(album.getName());
    }

    /**
     * Add a listen for the specified album
     *
     * @param albumName The name of the album for which we want to add a listen
     */
    public void addListen(final String albumName) {
        albumsListenTracker.addListen(albumName);
    }

    /**
     * Adds one listen to the specified song
     *
     * @param song The song for which we want to add a listen
     * @see Song
     */
    public void addListen(final Song song) {
        songsListenTracker.addListen(song.getName());
        genresListenTracker.addListen(song.getGenre());
        artistsListenTracker.addListen(song.getArtist());
    }

    /**
     * Add the specified number of listens for the song
     *
     * @param song  The song for which we want to add the listens
     * @param count The number of listens
     * @see Song
     */
    public void addListen(final Song song, final int count) {
        songsListenTracker.addListen(song.getName(), count);
        genresListenTracker.addListen(song.getGenre(), count);
        artistsListenTracker.addListen(song.getArtist(), count);
    }

    /**
     * Adds a listen for an Episode
     *
     * @param episode The Episode for which we want to add a listen
     * @see Episode
     */
    public void addListen(final Episode episode) {
        episodesListenTracker.addListen(episode.getName());
    }

    /**
     * Adds a listen for an audio file
     * (this is used to add a listen for a Song, an Episode or on Album,
     * for the other types of audio files, this does nothing)
     *
     * @param audio The audio file for which we want to add a listen
     * @param <E>   The class of the audio file (Important ones are Song, Episode and Album)
     * @see Song
     * @see Episode
     * @see Album
     */
    public <E extends Audio> void addListen(final E audio) {
        audio.addListen(this);
    }

    /**
     * Get the top listens for each category (artists, genres, songs, albums, episodes)
     * This is used with the wrapped command
     *
     * @return A map with the top results for each category
     */
    public HashMap<String, Object> topListensForEach() {
        HashMap<String, Object> result = new LinkedHashMap<>();

        result.put("topArtists", artistsListenTracker.getTopFiveListens());
        result.put("topGenres", genresListenTracker.getTopFiveListens());
        result.put("topSongs", songsListenTracker.getTopFiveListens());
        result.put("topAlbums", albumsListenTracker.getTopFiveListens());
        result.put("topEpisodes", episodesListenTracker.getTopFiveListens());
        return result;
    }
}
