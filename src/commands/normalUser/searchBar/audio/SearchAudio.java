package commands.normalUser.searchBar.audio;

import entities.audio.Audio;
import commands.normalUser.searchBar.Search;
import commands.normalUser.searchBar.SearchType;
import commands.normalUser.searchBar.filter.Filter;
import commands.normalUser.searchBar.filter.filterAudio.FilterSongs;
import commands.normalUser.searchBar.filter.filterAudio.filterCollections.FilterAlbums;
import commands.normalUser.searchBar.filter.filterAudio.filterCollections.FilterPlaylists;
import commands.normalUser.searchBar.filter.filterAudio.filterCollections.FilterPodcasts;
import libraries.audio.AlbumsLibrary;
import libraries.audio.PlaylistsLibrary;
import libraries.audio.PodcastsLibrary;
import libraries.audio.SongsLibrary;

import java.util.HashMap;

public final class SearchAudio extends Search<Audio> {
    private <E extends Audio> void putFilter(final SearchType searchType,
                                             final Filter<E> filter) {
        searchTypeFilter.put(searchType, filter);
    }
    @Override
    protected void initSearchTypeFilter() {
        if (searchTypeFilter == null) {
            searchTypeFilter = new HashMap<>();
        }
        putFilter(SearchType.song,
                new FilterSongs(SongsLibrary.getInstance().getItems()));
        putFilter(SearchType.podcast,
                new FilterPodcasts(PodcastsLibrary.getInstance().getItems()));
        putFilter(SearchType.playlist,
                new FilterPlaylists(PlaylistsLibrary.getInstance().getItems()));
        if (AlbumsLibrary.getInstance().getItems() != null) {
            putFilter(SearchType.album,
                    new FilterAlbums(AlbumsLibrary.getInstance().sortAlbumsByArtistOrder()));
        }
    }

    @Override
    public void addResults() {
        for (Audio audio : filteredObjects) {
            searchResults.add(audio.getName());
            if (searchResults.size() >= Search.MAX_NUM_OF_RESULTS) {
                break;
            }
        }
    }
}
