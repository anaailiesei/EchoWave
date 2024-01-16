package commands.normalUser.searchBar;

import commands.ActionCommand;
import commands.normalUser.searchBar.filter.Filter;
import commands.normalUser.searchBar.filter.Filters;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;

public abstract class Search<E> extends ActionCommand {
    protected static final int MAX_NUM_OF_RESULTS = 5;
    protected Map<SearchType, Filter<? extends E>> searchTypeFilter;
    @Getter
    protected ArrayList<String> searchResults;
    @Getter
    protected ArrayList<? extends E> filteredObjects;

    /**
     * Initialize mapping between the search type and the filter used
     */
    protected abstract void initSearchTypeFilter();

    /**
     * Executes a search operation based on the specified search type and filters,
     * then populates the searchResults list with the names of the matching entities.audio objects.
     *
     * @param type    The type of search to be performed
     * @param filters The filters used for searching
     */
    public final void execute(final String type, final Map<Filters, Object> filters) {
        SearchType searchType = SearchType.fromString(type);
        initSearchTypeFilter();
        searchResults = new ArrayList<>();
        Filter<? extends E> filter = searchTypeFilter.get(searchType);
        for (Map.Entry<Filters, Object> filter2 : filters.entrySet()) {
            Filters key = filter2.getKey();
            if (key == null) {
                break;
            }
            if (filter.getFilterMethods() == null) {
                break;
            }
            Function<Object, ? extends Filter<E>> function =
                    (Function<Object, ? extends Filter<E>>) filter.getFilterMethods().get(key);
            if (function == null) {
                break;
            }
            filter.getFilterMethods().get(key).apply(filter2.getValue());
        }
//        filters.forEach((key, value) -> filter.getFilterMethods().get(key).apply(value));
        filteredObjects = filter.keepFirstObjects(MAX_NUM_OF_RESULTS).getFilteredObjects();
        addResults();
        setMessage(toString());
    }

    /**
     * Add the results of the searching operation
     * searchResults should be set here
     */
    public abstract void addResults();

    /**
     * Resets the search parameters and results
     */
    public final void resetSearch() {
        filteredObjects = null;
        searchResults = null;
    }

    @Override
    public final String toString() {
        return "Search returned " + searchResults.size() + " results";
    }
}
