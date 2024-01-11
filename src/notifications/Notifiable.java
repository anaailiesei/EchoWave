package notifications;

public interface Notifiable {
    /**
     * Update the observer (notifiable) once a notification is received
     * The notification will be made by a {@code Notifier}
     *
     * @param notification The notification received
     * @see Notification
     * @see Notifier
     */
    void update(Notification notification);
}
