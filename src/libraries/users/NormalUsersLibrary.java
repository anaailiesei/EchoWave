package libraries.users;

import entities.audio.Song;
import entities.audio.collections.Playlist;
import libraries.GenericLibrary;
import libraries.audio.PlaylistsLibrary;
import entities.user.NormalUser;

import java.util.ArrayList;
import java.util.List;

public final class NormalUsersLibrary extends GenericLibrary<NormalUser> {
    private static NormalUsersLibrary instance = null;

    private NormalUsersLibrary() {
    }

    /**
     * Gets the instance for the users' library class (singleton pattern)
     * Initialize it if needed
     *
     * @return The library instance
     */
    public static synchronized NormalUsersLibrary getInstance() {
        if (instance == null) {
            instance = new NormalUsersLibrary();
        }
        return instance;
    }

    /**
     * Gets the entities.user by the specified username
     *
     * @param username The username to seacrh for
     * @return The entities.user
     */
    public NormalUser getUserByName(final String username) {
        if (getItems() == null) {
            return null;
        }
        return getItems()
                .stream()
                .filter(user -> user.getName().equals(username))
                .findFirst()
                .orElse(null);
    }

    public List<String> getOnlineUsers() {
        return getItems().stream()
                .filter(NormalUser::isOnline)
                .map(NormalUser::getName)
                .toList();
    }

    /**
     * Checks fi the specified entities.user exists
     *
     * @param username The name of the entities.user to be searched
     * @return {@code true} if the entities.user exists, {@code false} otherwise
     */
    public boolean userExists(final String username) {
        return getUserByName(username) != null;
    }

    /**
     * Deletes the given entities.user
     *  - Removes likes from songs liked by the entities.user
     *  - Deletes playlists owned by the entities.user
     *  - Removes followers associated with the entities.user
     *  - Removes follows from playlists followed by the entities.user
     *
     * @param user The entities.user to be deleted
     */
    public void deleteUser(final NormalUser user) {
        ArrayList<Playlist> playlists = user.getPlaylists();
        Playlist likedSongs = user.getLiked();
        ArrayList<Playlist> followedPlaylists = user.getFollowedPlaylists();
        if (likedSongs != null) {
            for (Song song : likedSongs.getCollection()) {
                song.removeLikeFrom(user);
            }
        }
        if (playlists != null) {
            for (Playlist playlist : playlists) {
                for (Song song : playlist.getCollection()) {
                    song.decrementInPlaylistCount();
                }
                playlist.removeAllFollowers();
                PlaylistsLibrary.getInstance().removePlaylist(playlist);
            }
        }
        if (followedPlaylists != null) {
            for (Playlist followedPlaylist : followedPlaylists) {
                followedPlaylist.removeFollower();
            }
        }
        removeItem(user);
    }
}
