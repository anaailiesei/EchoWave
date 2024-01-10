package audio;

import fileio.input.SongInput;
import lombok.Getter;
import statistics.ListenTrackerNormalUser;
import user.NormalUser;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Implementation for a songInput object
 */
public final class Song implements Audio {
    private final SongInput songInput;
    private final ArrayList<NormalUser> likedBy = new ArrayList<>();
    /**
     * -- GETTER --
     * Get the likes count for this song
     */
    @Getter
    private int likes;
    /**
     * -- GETTER --
     * Get the loaded times count for this song
     */
    @Getter
    private int loadedCount;
    /**
     * -- GETTER --
     * Get the number of playlists this song is part of
     */
    @Getter
    private int inPlaylistCount;

    public Song(final SongInput songInput) {
        this.songInput = songInput;
    }

    private Song(final Song song) {
        this(song.songInput);
        this.likes = song.getLikes();
        this.loadedCount = song.getLoadedCount();
        this.inPlaylistCount = song.getInPlaylistCount();
    }

    public Song(final String name,
                final Integer duration,
                final String album,
                final ArrayList<String> tags,
                final String lyrics,
                final String genre,
                final Integer releaseYear,
                final String artist) {
        this(new SongInput(name, duration, album, tags, lyrics, genre, releaseYear, artist));
    }

    /**
     * Get the name of the song
     *
     * @return the name
     */
    @Override
    public String getName() {
        return songInput.getName();
    }

    /**
     * Gets the duration of the song
     *
     * @return the duration
     */
    @Override
    public int getDuration() {
        return songInput.getDuration();
    }

    /**
     * Checks if the name of th song starts with the specified string
     *
     * @return {@code true} if the song starts with the given string, {@code false} otherwise
     */
    @Override
    public boolean nameStartsWith(final String searchString) {
        return songInput.getName().startsWith(searchString);
    }

    @Override
    public void addListen(final ListenTrackerNormalUser listenTracker) {
        listenTracker.addListen(this);
    }

    @Override
    public void addListen(final ListenTrackerNormalUser listenTracker, final int count) {
        listenTracker.addListen(this, count);
    }

    @Override
    public String getOwner() {
        return songInput.getArtist();
    }

    /**
     * Copies a song object
     *
     * @return an instance of the newly created song object
     */
    @Override
    public Song copyObject() {
        return new Song(this);
    }

    /**
     * Checks if a songInput is in the specified album
     *
     * @param album for the album the songInput should be part of
     * @return {@code true} if the songInput is in the specified album, {@code false} otherwise
     */
    public boolean inAlbum(final String album) {
        return songInput.getAlbum().equals(album);
    }

    /**
     * Checks if a songInput contains a specified tag
     *
     * @param tag for the tag that's being searched
     * @return {@code true} if the songInput has the specified tag, {@code false} otherwise
     */
    public boolean containsTag(final String tag) {
        return songInput.getTags().contains(tag);
    }

    /**
     * Checks if the songInput has any of the specified tags
     *
     * @param tags for the list of tags that are being searched
     * @return {@code true} if the songInput has at least one of the tags in the specified list,
     * {@code false} otherwise
     */
    public boolean containsTags(final ArrayList<String> tags) {
        for (String tag : tags) {
            if (!containsTag(tag)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the songInput contains the specified lyrics
     *
     * @param lyrics for the lyrics being searched
     * @return {@code true} if the lyrics are found in the songInput lyrics, {@code false} otherwise
     */
    public boolean containsLyrics(final String lyrics) {
        String lyricsToLower = lyrics.toLowerCase();
        return songInput.getLyrics().toLowerCase().contains(lyricsToLower);
    }

    /**
     * Checks if the songInput is part of the specified genre
     *
     * @param genre for the genre the songInput should be part of
     * @return {@code true} if the songInput is a part of the specified genre,
     * {@code false} otherwise
     */
    public boolean isGenre(final String genre) {
        return songInput.getGenre().equalsIgnoreCase(genre);
    }

    /**
     * Checks if the songInput was released before the specified year
     *
     * @param year The year we compare the release year with
     * @return {@code true} if the songInput was released before the specified year,
     * {@code false} otherwise
     */
    public boolean releasedBeforeYear(final Integer year) {
        return songInput.getReleaseYear() < year;
    }

    /**
     * Checks if the songInput was released after the specified year
     *
     * @param year The year we compare the release year with
     * @return {@code true} if the songInput was released after the specified year,
     * {@code false} otherwise
     */
    public boolean releasedAfterYear(final Integer year) {
        return songInput.getReleaseYear() > year;
    }

    /**
     * Checks if the songInput was created by the specified artist
     *
     * @param artist The artist for whom the songInput's creator is checked
     * @return {@code true} if the songInput was created by the specified artist,
     * {@code false} otherwise
     */
    public boolean createdBy(final String artist) {
        return songInput.getArtist().equals(artist);
    }

    /**
     * Increment the like count for this song
     */
    private void addLike() {
        likes++;
    }

    /**
     * Decrement the like count for this song
     */
    private void removeLike() {
        if (likes > 0) {
            likes--;
        }
    }

    public String getArtistName() {
        return songInput.getArtist();
    }

    /**
     * Increments the counter for the number of users that added this song to their playlist
     */
    public void incrementInPlaylistCount() {
        inPlaylistCount++;
    }

    /**
     * Decrements the counter for the number of users that added this song to their playlist
     */
    public void decrementInPlaylistCount() {
        inPlaylistCount--;
    }

    /**
     * Increments the counter for the number of users that are playing this song
     */
    public void incrementLoadedCount() {
        loadedCount++;
    }

    /**
     * Decrements the counter for the number of users that are playing this song
     */
    public void decrementLoadedCount() {
        loadedCount--;
    }

    /**
     * Removes the like from the given user
     *
     * @param user The liker
     */
    public void removeLikeFrom(final NormalUser user) {
        likedBy.remove(user);
        user.removeLikedSong(this);
        removeLike();
    }

    /**
     * Adds a like from the given user
     *
     * @param user The liker
     */
    public void addLikeFrom(final NormalUser user) {
        likedBy.add(user);
        user.addLikedSong(this);
        addLike();
    }

    /**
     * Removes all likes for the songs (from the users liked playlist)
     */
    public void removeAllLikes() {
        Iterator<NormalUser> iterator = likedBy.iterator();
        while (iterator.hasNext()) {
            NormalUser liker = iterator.next();
            liker.removeLikedSong(this);
            removeLike();
            iterator.remove();
        }
    }

    @Override
    public String toString() {
        return getName() + " - " + getArtistName();
    }

    /**
     * Gets the album name of this song
     *
     * @return The name of the album
     */
    public String getAlbum() {
        return songInput.getAlbum();
    }

    public String getArtist() {
        return songInput.getArtist();
    }

    public String getGenre() {
        return songInput.getGenre();
    }
}
