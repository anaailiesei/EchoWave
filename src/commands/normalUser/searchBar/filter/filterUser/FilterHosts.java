package commands.normalUser.searchBar.filter.filterUser;

import entities.user.Host;

import java.util.ArrayList;

public class FilterHosts extends FilterUser<Host> {
    public FilterHosts(final ArrayList<Host> hosts) {
        this.filteredObjects = new ArrayList<>(hosts);
        initAllFilterMethods();
    }
}
