package commands.normalUser.searchBar.filter.filterAudio.filterCollections;

import audio.collections.Album;
import commands.normalUser.searchBar.filter.Filters;

import java.util.ArrayList;
import java.util.HashMap;

public final class FilterAlbums extends FilterCollections<Album> {
    public FilterAlbums(final ArrayList<Album> albums) {
        this.filteredObjects = new ArrayList<>(albums);
        initAllFilterMethods();
    }

    @Override
    public void initAllFilterMethods() {
        filterMethods = new HashMap<>();
        filterMethods.put(Filters.name, this::byName);
        filterMethods.put(Filters.owner, this::byOwner);
    }
}
