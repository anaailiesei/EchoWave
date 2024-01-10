package commands.normalUser.searchBar.filter.filterAudio;

import entities.audio.Audio;
import commands.normalUser.searchBar.filter.Filter;


/**
 * For filtering {@code Audio} collections or arrays
 *
 * @param <E> The collection class or the class of the objects from within the array
 */
public abstract class FilterAudio<E extends Audio> extends Filter<E> {

    /**
     * Filters objects by their names
     *
     * @param nameObj The name we search for
     * @return a list of objects that start with the name specified
     */

    public final <T extends Filter<E>> T byName(final Object nameObj) {
        String name = (String) nameObj;
        filteredObjects.removeIf(object -> !object.nameStartsWith(name));
        return (T) this;
    }
}
