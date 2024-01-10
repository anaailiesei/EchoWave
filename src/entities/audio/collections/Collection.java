package entities.audio.collections;

import entities.audio.Audio;
import lombok.Getter;

import java.util.ArrayList;

/**
 * Class for an entities.audio collection
 */
@Getter
public class Collection<E extends Audio> implements Audio {
    protected int loadedCount;
    /**
     * -- GETTER --
     *  Get the items in a collection
     */
    protected ArrayList<E> collection;
    /**
     * -- GETTER --
     *  Get the name of the collection
     */
    private String name;
    /**
     * -- GETTER --
     *  Gets the owner name of th collection
     */
    private String owner;

    public Collection() {
    }

    public Collection(final Collection<E> genericCollection) {
        this.name = genericCollection.name;
        this.owner = genericCollection.owner;
        this.collection = genericCollection.collection;
        this.loadedCount = genericCollection.loadedCount;
    }

    /**
     * Set the items in a collection
     *
     * @param collection The list of items to be set
     */
    public void setCollection(final ArrayList<E> collection) {
        this.collection = collection;
    }

    /**
     * Sets the name of the collection
     *
     * @param name The name to be set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the owner name for the collection
     *
     * @param owner The name of the owner
     */
    public void setOwner(final String owner) {
        this.owner = owner;
    }

    /**
     * Verify if the owner of the item is the one specified
     *
     * @param otherOwner for the owner name
     * @return true if th owner is the one specified, false otherwise
     */
    public boolean ownerIs(final String otherOwner) {
        return otherOwner.equals(owner);
    }

    /**
     * Verify if the name of th collection starts with the given string
     *
     * @param searchString for the string that's being searched
     * @return {@code true} if collection's name starts with the given string,
     * {@code false} otherwise
     */
    @Override
    public boolean nameStartsWith(final String searchString) {
        return this.name.startsWith(searchString);
    }


    /**
     * Copies the collection given as an argument
     *
     * @return The copy
     */
    @Override
    public Collection<E> copyObject() {
        return new Collection<>(this);
    }

    /**
     * Checks if the collection is empty
     *
     * @return {@code true} if the collection is empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return collection.isEmpty();
    }

    /**
     * Checks if the specified item is contained in the collection (podcast or playlist)
     *
     * @param item The item we check for
     * @return {@code true} if the collection contains the specified item,
     * {@code false} otherwise
     */
    public boolean itemExists(final E item) {
        return collection.contains(item);
    }

    /**
     * Adds the specified item to the collection
     *
     * @param item The item to be addded
     */
    public void addItem(final E item) {
        collection.add(item);
    }

    /**
     * Removes the specified item from the collection
     *
     * @param item The item to be removed
     */
    public void removeItem(final E item) {
        collection.remove(item);
    }

    /**
     * Checks if item exists in the collection
     *
     * @param item The item to search for
     * @return {@code true} if the item exists, {@code false} otherwise
     */
    public boolean containsItem(final E item) {
        return collection.contains(item);
    }

    /**
     * Get the size (number of items) of the collection
     *
     * @return the size
     */
    public int getSize() {
        return collection.size();
    }

    /**
     * Increments the counter for the number of users that are playing this podcast
     */
    public void incrementLoadedCount() {
        loadedCount++;
    }

    /**
     * Decrements the counter for the number of users that are playing this podcast
     */
    public void decrementLoadedCount() {
        loadedCount--;
    }

    /**
     * Checks if the collection is deletable.
     * For the collection to be deletable, none of the normal users should have it loaded
     * in their player
     * Method might be Overridden to add new conditions for deletion for a particular collection
     *
     * @return {@code true} if the collection is deletable, {@code false} otherwise
     */
    public boolean isDeletable() {
        return loadedCount == 0;
    }
}
