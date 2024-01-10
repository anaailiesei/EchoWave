package commands.normalUser.searchBar.filter.filterAudio.filterCollections;

import audio.collections.Podcast;
import commands.normalUser.searchBar.filter.Filters;

import java.util.ArrayList;
import java.util.HashMap;

public class FilterPodcasts extends FilterCollections<Podcast> {
    public FilterPodcasts(final ArrayList<Podcast> podcasts) {
        this.filteredObjects = new ArrayList<>(podcasts);
        initAllFilterMethods();
    }

    @Override
    public final void initAllFilterMethods() {
        filterMethods = new HashMap<>();
        filterMethods.put(Filters.name, this::byName);
        filterMethods.put(Filters.owner, this::byOwner);
    }
}
