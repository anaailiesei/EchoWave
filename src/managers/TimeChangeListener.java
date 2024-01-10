package managers;

/**
 * Interface for managing time changes
 */
public interface TimeChangeListener {
    /**
     * Describes what to do when the listener is notified with a time change
     *
     * @param timeDifference the difference in time between old timestamp and new timestamp
     */
    void onTimeChanged(int timeDifference);
}
