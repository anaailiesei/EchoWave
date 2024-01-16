package managers;

import lombok.Getter;

import java.util.ArrayList;

/**
 * Class for time management
 */
public final class TimeManager {
    private static TimeManager instance = null;
    @Getter
    private int currentTime = 0;
    private final ArrayList<TimeChangeListener> timeChangeListeners = new ArrayList<>();

    private TimeManager() { }

    /**
     * Initialize and get the instance for the {@code TimeManagement} class
     *
     * @return the instance
     */
    public static synchronized TimeManager getInstance() {
        if (instance == null) {
            instance = new TimeManager();
        }
        return instance;
    }

    /**
     * Gets the current time
     *
     * @return the time
     */
    public int getTime() {
        return currentTime;
    }

    /**
     * Sets the current time to the timestamp of a command
     *
     * @param timeStamp the time to be set
     */
    public synchronized void setTime(final int timeStamp) {
        int timeDifference = timeStamp - currentTime;
        this.currentTime = timeStamp;
        notifyTimeChangeListeners(timeDifference);
    }

    /**
     * Adds a time change listener to the list of listeners
     * @param listener the listener to be added
     */
    public synchronized void addTimeChangeListener(final TimeChangeListener listener) {
        timeChangeListeners.add(listener);
    }

    /**
     * Removes a listener from the list of time change listeners
     * @param listener the listener to be removed
     */
    public synchronized void removeTimeChangeListener(final TimeChangeListener listener) {
        timeChangeListeners.remove(listener);
    }

    /**
     * Notify all time change listeners with the time difference
     *
     * @param timeDifference the difference between the old time and the new current time
     */
    private synchronized void notifyTimeChangeListeners(final int timeDifference) {
        for (TimeChangeListener listener : timeChangeListeners) {
            listener.onTimeChanged(timeDifference);
        }
    }
}
