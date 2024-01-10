package libraries.audio;

import audio.collections.Collection;
import audio.collections.Playlist;
import commands.normalUser.searchBar.filter.filterAudio.filterCollections.FilterPlaylists;
import libraries.GenericLibrary;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class PlaylistsLibrary extends GenericLibrary<Playlist> {
    private static PlaylistsLibrary instance = null;

    private PlaylistsLibrary() {
    }

    /**
     * Gets the instance for the playlists library class (singleton pattern)
     * Initialize it if needed
     *
     * @return The library instance
     */
    public static synchronized PlaylistsLibrary getInstance() {
        if (instance == null) {
            instance = new PlaylistsLibrary();
        }
        return instance;
    }

    /**
     * Removes the specified playlist from the library
     * @param playlist The playlist to be removed
     */
    public void removePlaylist(final Playlist playlist) {
        removeItem(playlist);
    }

    /**
     * Checks if the specified playlist already exists in user's created playlists list
     *
     * @param playlistName The playlist to check for
     * @param username     The user in whose list of playlists we search in
     * @return {@code true} if the playlist already exists, {@code false} otherwise
     */
    public boolean playlistForUserExists(final String playlistName, final String username) {
        if (getItems() == null) {
            return false;
        }
        ArrayList<Playlist> playlistsForUser = (ArrayList<Playlist>) getPlaylistsForUser(username);
        FilterPlaylists filterPlaylists = new FilterPlaylists(playlistsForUser);
        ArrayList<Collection<?>> playlistsWithName =
                filterPlaylists.byName(playlistName).getFilteredObjects();
        return playlistsWithName.isEmpty();
    }

    /**
     * Gets the playlists that the specified user owns
     *
     * @param username The user for whose playlists we search
     * @return an array list with the user's playlists
     */
    public ArrayList<? extends Collection<?>> getPlaylistsForUser(final String username) {
        if (getItems() == null) {
            return null;
        }
        FilterPlaylists filterPlaylists = new FilterPlaylists(getItems());
        ArrayList<? extends Collection<?>> playlists =
                filterPlaylists.byOwner(username).getFilteredObjects();
        return playlists;
    }

    private ArrayList<? extends Collection<?>> getPublicPlaylistsForUser(final String username) {
        if (getItems() == null) {
            return null;
        }
        FilterPlaylists filterPlaylists = new FilterPlaylists(getItems());
        return filterPlaylists.byVisibilityForUser(username).getFilteredObjects();
    }

    /**
     * Filters only public playlists
     *
     * @return A list of public playlists
     */
    private ArrayList<? extends Collection<?>> getPublicPlaylists() {
        if (getItems() == null) {
            return null;
        }
        FilterPlaylists filterPlaylists = new FilterPlaylists(getItems());
        return filterPlaylists.byVisibility().getFilteredObjects();
    }

    /**
     * Sort the given list of playlists by followers in descending order
     *
     * @param playlists the list of playlists to be sorted
     * @return The sorted list
     */
    private ArrayList<Playlist> sortPlaylistsByFollowers(final ArrayList<Playlist> playlists) {
        if (playlists == null) {
            return null;
        }
        playlists.sort(Comparator.comparing(Playlist::getFollowers).reversed());
        return playlists;
    }

    /**
     * Gets the top playlists by followers and keeps only the specified number of playlists
     *
     * @param maxNumber The maximum number of playlists to keep
     * @return The list of playlists with the most followers
     */
    public ArrayList<Playlist> getTopPlaylists(final int maxNumber) {
        ArrayList<Playlist> publicPlaylists = (ArrayList<Playlist>) getPublicPlaylists();
        ArrayList<Playlist> sortedPublicPlaylists = sortPlaylistsByFollowers(publicPlaylists);
        if (sortedPublicPlaylists != null) {
            if (sortedPublicPlaylists.size() > maxNumber) {
                List<Playlist> firstPlaylists = sortedPublicPlaylists.subList(0, maxNumber);
                return new ArrayList<>(firstPlaylists);
            }
        }
        return sortedPublicPlaylists;
    }
}
