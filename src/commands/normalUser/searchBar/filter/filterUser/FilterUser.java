package commands.normalUser.searchBar.filter.filterUser;

import commands.normalUser.searchBar.filter.Filter;
import commands.normalUser.searchBar.filter.Filters;
import entities.user.User;

import java.util.HashMap;

public class FilterUser<E extends User> extends Filter<User> {
    /**
     * Filters the users by their username
     *
     * @param nameObj The name object (and object with the username)
     * @param <T>     The filtering class that will extend the FilterUser
     * @return The current filter instance
     * @see Filter
     * @see FilterHosts
     * @see FilterArtists
     */
    public <T extends FilterUser<E>> T byUsername(final Object nameObj) {
        String name = (String) nameObj;
        filteredObjects.removeIf(object -> !object.usernameStartsWith(name));
        return (T) this;
    }

    @Override
    public final void initAllFilterMethods() {
        filterMethods = new HashMap<>();
        filterMethods.put(Filters.name, this::byUsername);
    }
}
