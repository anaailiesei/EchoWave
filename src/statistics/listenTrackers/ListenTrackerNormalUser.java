package statistics.listenTrackers;

import entities.NeamableEntity;
import entities.audio.Audio;
import entities.audio.Episode;
import entities.audio.Song;
import entities.audio.collections.Album;
import entities.user.Artist;
import libraries.audio.AlbumsLibrary;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

public final class ListenTrackerNormalUser {
    private boolean isPremium;
    private final ListenTracker<NeamableEntity> artistsListenTracker = new ListenTracker<>();
    private final ListenTracker<Album> albumsListenTracker = new ListenTracker<>();
    private final ListenTracker<Song> songsListenTracker = new ListenTracker<>();
    private final ListenTracker<Episode> episodesListenTracker = new ListenTracker<>();
    private final ListenTracker<NeamableEntity> genresListenTracker = new ListenTracker<>();
    private final ListenTracker<Song> premiumListenTracker = new ListenTracker<>();

    private final ListenTracker<Song> freeListenTracker = new ListenTracker<>();

    /**
     * Adds a listen or the specified album
     *
     * @param album The album for which we add a listen
     * @see Album
     */
    public void addListen(final Album album) {
        albumsListenTracker.addListen(album);
    }

    /**
     * Add a listen for the specified album
     *
     * @param albumName The name of the album for which we want to add a listen
     */
    public void addListen(final String albumName) {
        Album album = AlbumsLibrary.getInstance().getAlbumByName(albumName);
        albumsListenTracker.addListen(album);
    }

    /**
     * Adds one listen to the specified song
     * If the song is premium, it also adds a listen for this song in the premium
     * listened songs
     *
     * @param song The song for which we want to add a listen
     * @see Song
     */
    public void addListen(final Song song) {
        if (isPremium) {
            premiumListenTracker.addListen(song);
        } else {
            freeListenTracker.addListen(song);
        }
        songsListenTracker.addListen(song);
        genresListenTracker.addListen(new NeamableEntity(song.getGenre()));
        artistsListenTracker.addListen(new NeamableEntity(song.getArtist()));
    }

    /**
     * Add the specified number of listens for the song
     * If the song is premium, it also adds the specified number of listens for this song in the premium
     * listened songs
     *
     * @param song  The song for which we want to add the listens
     * @param count The number of listens
     * @see Song
     */
    public void addListen(final Song song, final int count) {
        if (isPremium) {
            premiumListenTracker.addListen(song, count);
        } else {
            freeListenTracker.addListen(song, count);
        }
        songsListenTracker.addListen(song, count);
        genresListenTracker.addListen(new NeamableEntity(song.getGenre()), count);
        artistsListenTracker.addListen(new NeamableEntity(song.getArtist()), count);
    }

    /**
     * Adds a listen for an Episode
     *
     * @param episode The Episode for which we want to add a listen
     * @see Episode
     */
    public void addListen(final Episode episode) {
        episodesListenTracker.addListen(episode);
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

    /**
     * Checks if the user hasn't listened to anything yet
     *
     * @return {@code true} if the user hasn't listened anything, {@code false} otherwise
     */
    public boolean noListens() {
        return artistsListenTracker.isEmpty()
                && genresListenTracker.isEmpty()
                && songsListenTracker.isEmpty()
                && albumsListenTracker.isEmpty()
                && episodesListenTracker.isEmpty();
    }

    /**
     * This method updates the premium status of the user to the specified value.
     *
     * @param premium {@code true} if the user is to be set as premium, {@code false} otherwise.
     */
    public void setPremium(boolean premium) {
        this.isPremium = premium;
    }

    /**
     * Get a map with the songs listened during premium subscription
     *
     * @return A tree map with the premium listened songs ordered by their name
     */
    public TreeMap<Song, Integer> getPremiumSongs() {
        return premiumListenTracker.getListens();
    }
    /**
     * Get a map with the free songs listened between two ad breaks
     *
     * @return A tree map with the free listened songs ordered by their name
     */
    public TreeMap<Song, Integer> getFreeSongs() {
        return freeListenTracker.getListens();
    }

    /**
     * Empties the free songs tracker
     */
    public void emptyFreeSongs() {
        freeListenTracker.clear();
    }

    /**
     * Empties the premium songs tracker
     */
    public void emptyPremiumSongs() {
        premiumListenTracker.clear();
    }
}
