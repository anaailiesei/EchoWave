package audio;

import statistics.ListenTrackerNormalUser;

/**
 * Interface for an audio file
 */
public interface Audio extends Cloneable {
    /**
     * Gets the name of the audio object
     *
     * @return the name
     */
    String getName();

    /**
     * Gets the duration of the audio object
     *
     * @return the duration;
     */
    default int getDuration() {
        return 0;
    }

    /**
     * Verify if the name of the item begins with the given string
     *
     * @param searchString for the string that's being searched
     * @return {@code true} if the item's name starts with the given string, {@code false} otherwise
     */
    boolean nameStartsWith(String searchString);

    /**
     * Copies an object of {@code Audio} type
     *
     * @param <E> The class of the object to be copied
     * @return The copy
     */
    <E extends Audio> E copyObject();

    /**
     * Adds a listen for the current audio file in the specified listen tracker
     * (owned by a user)
     *
     * @param listenTracker The user's listen tracker
     */
    default void addListen(ListenTrackerNormalUser listenTracker) {
    }

    /**
     * Adds the specified number of listens for the current audio file
     * in the specified listen tracker (owned by a normal user)
     *
     * @param listenTracker The user's listen tracker
     */
    default void addListen(ListenTrackerNormalUser listenTracker, int count) {
    }

    /**
     * Gets the name of the owner for the audio file
     *
     * @return A {@code String} with the name of the owner
     */
    String getOwner();
}
