package commands.normalUser.searchBar.filter.filterAudio.filterCollections;

import entities.audio.Audio;
import entities.audio.collections.Collection;
import commands.normalUser.searchBar.filter.filterAudio.FilterAudio;

public abstract class FilterCollections<E extends Collection<? extends Audio>>
        extends FilterAudio<Collection<? extends Audio>> {
    /**
     * Filters collections by their owner
     *
     * @param ownerObj The name of the owner we search for
     * @return current instance
     */
    public final <T extends FilterCollections<E>> T byOwner(final Object ownerObj) {
        String owner = (String) ownerObj;
        filteredObjects.removeIf(object -> !object.ownerIs(owner));
        return (T) this;
    }
}
