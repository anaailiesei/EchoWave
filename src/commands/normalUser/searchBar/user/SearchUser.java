package commands.normalUser.searchBar.user;

import commands.normalUser.searchBar.Search;
import commands.normalUser.searchBar.SearchType;
import commands.normalUser.searchBar.filter.Filter;
import commands.normalUser.searchBar.filter.filterUser.FilterArtists;
import commands.normalUser.searchBar.filter.filterUser.FilterHosts;
import libraries.users.ArtistsLibrary;
import libraries.users.HostsLibrary;
import user.Artist;
import user.Host;
import user.User;

import java.util.ArrayList;
import java.util.HashMap;

public final class SearchUser extends Search<User> {
    private <E extends User> void putFilter(final SearchType searchType, final Filter<E> filter) {
        searchTypeFilter.put(searchType, filter);
    }

    @Override
    protected void initSearchTypeFilter() {
        if (searchTypeFilter == null) {
            searchTypeFilter = new HashMap<>();
        }
        ArrayList<Artist> artists = ArtistsLibrary.getInstance().getItems();
        if (artists != null) {
            putFilter(SearchType.artist, new FilterArtists(artists));
        }
        ArrayList<Host> hosts = HostsLibrary.getInstance().getItems();
        if (hosts != null) {
            putFilter(SearchType.host, new FilterHosts(HostsLibrary.getInstance().getItems()));
        }
    }

    @Override
    public void addResults() {
        for (User user : filteredObjects) {
            searchResults.add(user.getUsername());
            if (searchResults.size() >= Search.MAX_NUM_OF_RESULTS) {
                break;
            }
        }
    }
}
