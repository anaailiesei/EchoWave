package audio.collections;

import audio.Song;
import lombok.Getter;
import statistics.ListenTrackerNormalUser;

import java.util.ArrayList;

@Getter
public final class Album extends Collection<Song> {
    private int releaseYear;
    private String description;
    private int totalLikes;

    private Album(final String name, final String owner, final ArrayList<Song> songs) {
        setName(name);
        setOwner(owner);
        setCollection(songs);
    }

    public Album(final String name,
                 final String owner,
                 final int releaseYear,
                 final String description,
                 final ArrayList<Song> songs) {
        this(name, owner, songs);
        setReleaseYear(releaseYear);
        setDescription(description);
    }

    public void setReleaseYear(final int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Removes all likes for all the songs in the album
     */
    public void removeAllLikes() {
        for (Song song : getCollection()) {
            song.removeAllLikes();
        }
    }

    /**
     * Checks if the album is deletable
     * To be deletable, it shouldn't have any of its songs playing in a user's player,
     * the album itself shouldn't be played by a user,
     * and neither of its songs should be part of a user's playlist
     *
     * @return {@code true} if the album is deletable, {@code false} otherwise
     */
    @Override
    public boolean isDeletable() {
        if (getLoadedCount() > 0) {
            return false;
        }

        for (Song song : getCollection()) {
            if (song.getInPlaylistCount() > 0
                    || song.getLoadedCount() > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Increments the likes counter for this album (aka the sum of the likes each song
     * has on the album)
     */
    public void addLike() {
        totalLikes++;
    }

    /**
     * Decrements the total likes counter for this album (aka the sum of the likes each
     * song has on the album)
     */
    public void removeLike() {
        totalLikes--;
    }

    @Override
    public void addListen(final ListenTrackerNormalUser listenTracker) {
        listenTracker.addListen(this);
    }
}
