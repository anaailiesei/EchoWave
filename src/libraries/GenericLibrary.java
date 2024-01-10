package libraries;

import java.util.ArrayList;

public class GenericLibrary<E> {
    private ArrayList<E> items;

    protected GenericLibrary() { }

    /**
     * Gets the items in the library: songs, podcasts or playlists
     *
     * @return the items' list
     */
    public ArrayList<E> getItems() {
        return items;
    }

    /**
     * Sets the items in the library: songs, podcasts or playlists
     *
     * @param items The list of items to be set
     */
    public void setItems(final ArrayList<E> items) {
        this.items = items;
    }

    /**
     * Adds the specified item to the library
     * It initialises the library's elements' list if needed
     * @param item The item to be added
     */
    public void addItem(final E item) {
        if (items == null) {
            items = new ArrayList<>();
        }
            items.add(item);
    }

    /**
     * Removes the specified item from the library
     * @param item The item to be removed
     */
    public void removeItem(final E item) {
        items.remove(item);
    }

    /**
     * Copies the items list
     *
     * @return the copy
     */

    public ArrayList<E> copyItemsList() {
        return new ArrayList<>(items);
    }
}
