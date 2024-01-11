package notifications;

public interface Notifier {
    /**
     * Adds an observer for the notifier
     *
     * @param observer The observer that should be added
     * @see Notifiable
     */
    void addObserver(Notifiable observer);

    /**
     * Removes an observer from the notifier
     *
     * @param observer The observer that should be removed
     * @see Notifiable
     */
    void removeObserver(Notifiable observer);

    /**
     * Notify all observers from the notifier
     *
     * @param notification The notification that should be sent to users
     */
    void notifyObservers(Notification notification);
}

