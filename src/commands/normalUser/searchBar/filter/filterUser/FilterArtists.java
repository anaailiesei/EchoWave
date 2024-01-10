package commands.normalUser.searchBar.filter.filterUser;

import user.Artist;

import java.util.ArrayList;

public class FilterArtists extends FilterUser<Artist> {
    public FilterArtists(final ArrayList<Artist> artists) {
        this.filteredObjects = new ArrayList<>(artists);
        initAllFilterMethods();
    }
}
