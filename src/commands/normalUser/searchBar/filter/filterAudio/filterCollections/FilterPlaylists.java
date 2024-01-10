package commands.normalUser.searchBar.filter.filterAudio.filterCollections;

import entities.audio.Audio;
import entities.audio.collections.Collection;
import entities.audio.collections.Playlist;
import commands.normalUser.searchBar.filter.Filters;
import commands.normalUser.searchBar.filter.filterAudio.FilterAudio;

import java.util.ArrayList;
import java.util.HashMap;

public class FilterPlaylists extends FilterCollections<Playlist> {
    public FilterPlaylists(final ArrayList<Playlist> playlists) {
        this.filteredObjects = new ArrayList<>(playlists);
        initAllFilterMethods();
    }

    @Override
    public final void initAllFilterMethods() {
        filterMethods = new HashMap<>();
        filterMethods.put(Filters.name, this::byName);
        filterMethods.put(Filters.owner, this::byOwner);
        filterMethods.put(Filters.visibilityForUser, this::byVisibilityForUser);
    }

    /**
     * Filters playlists by visibility for a specified entities.user
     * It filters out private playlists that the entities.user doesn't own
     * (Meaning owned playlists by the specified entities.user are being kept)
     *
     * @param username The name of the entities.user that performs the action
     * @return the current instance
     */
    public FilterAudio<Collection<? extends Audio>> byVisibilityForUser(final Object username) {
        filteredObjects.removeIf(playlistObject -> {
            Playlist playlist = (Playlist) playlistObject;
            return playlist.isPrivate() && !playlist.getOwner().equals(username);
        });
        return this;
    }

    /**
     * Filters playlists by visibility (it filters out private playlists)
     *
     * @return the current instance
     */
    public FilterAudio<Collection<? extends Audio>> byVisibility() {
        filteredObjects.removeIf(playlist -> ((Playlist) playlist).isPrivate());
        return this;
    }
}
