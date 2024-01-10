package commands.normalUser.searchBar.audio;

import entities.audio.Audio;
import commands.normalUser.searchBar.Select;
import managers.normalUser.SearchBarManager;

import java.util.ArrayList;

public final class SelectAudio extends Select<Audio> {
    public SelectAudio(final SearchBarManager searchBarManager) {
        super.searchBarManager = searchBarManager;
    }
    /**
     * Overloading of {@code toString} method
     *
     * @param itemNumber the item number we check if it's valid
     * @param selectFrom the list of items we should select from
     * @return the result message of the operation
     */
    @Override
    public String toString(final int itemNumber, final ArrayList<? extends Audio> selectFrom) {
        if (!(super.searchBarManager.getStatus().
                equals(SearchBarManager.SearchBarStatus.searching))) {
            return "Please conduct a search before making a selection.";
        } else if (!isValid(itemNumber, selectFrom)) {
            return "The selected ID is too high.";
        } else {
            return "Successfully selected " + selectedObject.getName() + ".";
        }
    }
}
