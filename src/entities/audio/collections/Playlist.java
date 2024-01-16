package entities.audio.collections;

import entities.audio.Song;
import entities.user.NormalUser;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * Implementation for a playlist object
 */
public final class Playlist extends Collection<Song> {
    private final ArrayList<NormalUser> followedBy = new ArrayList<>();
    /**
     * -- GETTER --
     * Get the visibility for this Playlist (either public or private)
     */
    @Getter
    private String visibility = Visibility.PUBLIC.getValue();
    /**
     * -- GETTER --
     * Gets the followers count for this playlist
     */
    @Getter
    private int followers = 0;

    public Playlist(final String name, final String owner, final ArrayList<Song> songs) {
        setName(name);
        setOwner(owner);
        setCollection(songs);
    }

    /**
     * Sets the visibility for the current playlist based on the Visibility enum.
     * The actual visibility value is stored as a String
     *
     * @param visibility the visibility to be set (public or private)
     * @see Visibility
     * @see #getVisibility()
     */
    public void setVisibility(final Visibility visibility) {
        this.visibility = visibility.getValue();
    }

    /**
     * Get a lost of the song names from the playlist
     *
     * @return The list of song names
     */
    public ArrayList<String> getSongNames() {
        return getCollection().stream()
                .map(Song::getName)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Increment the followers count for this playlist
     */
    public void addFollower() {
        followers++;
    }

    /**
     * Decrement the followers count for this playlist
     */
    public void removeFollower() {
        if (followers > 0) {
            followers--;
        }
    }

    /**
     * Switches between playlist visibilities (public or private)
     *
     * @see Visibility
     */
    public void switchVisibility() {
        if (getVisibility().equals(Visibility.PUBLIC.getValue())) {
            setVisibility(Visibility.PRIVATE);
        } else {
            setVisibility(Visibility.PUBLIC);
        }
    }

    /**
     * Checks if the current playlist is private
     *
     * @return {@code true} if the playlist is private, {@code false} otherwise
     * @see Visibility
     * @see #getVisibility()
     */
    public boolean isPrivate() {
        return getVisibility().equals(Visibility.PRIVATE.getValue());
    }

    @Override
    public String toString() {
        return getName() + " - " + getOwner();
    }

    /**
     * Adds the specified follower.
     * It also adds this playlist to the follower's list of followed playlists.
     *
     * @param user the follower
     */
    public void addFollowFrom(final NormalUser user) {
        followedBy.add(user);
        user.addFollowedPlaylist(this);
        addFollower();
    }

    /**
     * Removes the specified album follower.
     * It also removes the playlist from the follower's lis of followed playlist
     *
     * @param user the follower
     */
    public void removeFollowFrom(final NormalUser user) {
        followedBy.remove(user);
        user.removeFollowedPlaylist(this);
        removeFollower();
    }

    /**
     * Removes all followers from this playlist.
     * It also removes the playlist from the followers' list of followed playlists
     */
    public void removeAllFollowers() {
        Iterator<NormalUser> iterator = followedBy.iterator();
        while (iterator.hasNext()) {
            NormalUser follower = iterator.next();
            follower.removeFollowedPlaylist(this);
            removeFollower();
            iterator.remove();
        }
    }

    /**
     * Checks if the playlist contains a song judging by the song name
     *
     * @param songName The name of the song
     * @return {@code true} if the playlist contains a song with the given name,
     * {@code false} otherwise
     */
    public boolean contains(final String songName) {
        return collection.stream().anyMatch(song -> song.getName().equals(songName));
    }
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Playlist)) {
            return false;
        }
        Playlist playlist = (Playlist) obj;

        if (!getName().equals(playlist.getName())) {
            return false;
        }

        if (!getOwner().equals(playlist.getOwner())) {
            return false;
        }

        for (Song song1 : playlist.getCollection()) {
            for (Song song2 : this.getCollection()) {
                if (!song1.equals(song2)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
