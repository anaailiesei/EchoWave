package commands.normalUser.searchBar;

import commands.ActionCommand;
import lombok.Getter;
import managers.normalUser.SearchBarManager;

import java.util.ArrayList;

public abstract class Select<E> extends ActionCommand {
    @Getter
    protected E selectedObject = null;
    protected SearchBarManager searchBarManager;

    /**
     * Overloading of {@code toString} method
     *
     * @param itemNumber the item number we check if it's valid
     * @param selectFrom the list of items we should select from
     * @return the result message of the operation
     */
    public abstract String toString(int itemNumber, ArrayList<? extends E> selectFrom);

    /**
     * Checks if the specified ID is valid
     *
     * @param itemNumber The ID to be checked
     * @return {@code true} id the ID is valid, {@code false} otherwise
     */
    protected boolean isValid(final int itemNumber, final ArrayList<? extends E> selectFrom) {
        if (selectFrom != null) {
            return itemNumber <= selectFrom.size();
        }
        return false;
    }

    /**
     * Reset the selection
     */
    public void resetSelect() {
        selectedObject = null;
    }

    /**
     * Method used when there are additional steps to be done in the selection process
     * for the specified user (this should be Overridden when making a user selection)
     *
     * @param username The name of the user
     */
    public void updateUser(final String username) {
    }

    /**
     * Executes the select operation selecting from the results of the search operation
     * Search must be performed beforehand
     *
     * @param itemNumber the index of the audio file to be selected
     * @param selectFrom the list of audio files we should select from
     */
    public final void execute(final int itemNumber,
                              final ArrayList<? extends E> selectFrom,
                              final String username) {
        if (searchBarManager.getStatus().equals(SearchBarManager.SearchBarStatus.searching)
                && isValid(itemNumber, selectFrom)) {
            selectedObject = selectFrom.get(itemNumber - 1);
        } else {
            selectedObject = null;
        }
        updateUser(username);
        setMessage(toString(itemNumber, selectFrom));
    }
}
