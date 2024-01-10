package commands.normalUser.searchBar.filter;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Getter
public abstract class Filter<E> {
    /**
     * -- GETTER --
     * Get the mapping between the filtering methods and the filters applied
     */
    protected Map<Filters, Function<Object, ? extends Filter<E>>> filterMethods;
    /**
     * -- GETTER --
     * Gets the objects resulted after filtering them
     */
    protected ArrayList<E> filteredObjects;

    /**
     * Keeps only the first specified number of results after filtering was done
     *
     * @param <T>   the class of the objects that were filtered
     * @param count the maximum number of objects to keep
     * @return a list of filtered objects
     */
    public final <T extends Filter<E>> T keepFirstObjects(final int count) {
        if (filteredObjects.size() > count) {
            List<E> firstObjects = filteredObjects.subList(0, count);
            filteredObjects.retainAll(firstObjects);
        }
        return (T) this;
    }

    /**
     * Initializes the filter methods aka the mapping between the filtering
     * method and the filter applied.
     * Initialize and put values into the filterMethods map
     *
     * @see Filters
     */
    public abstract void initAllFilterMethods();
}
