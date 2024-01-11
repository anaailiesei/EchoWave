package notifications;

import java.util.HashMap;

public final class Notification {

    private Notification() {
    }

    /**
     * Gets the notification output
     * @param notificationType The type of the notification
     * @param notifier The name of the notifier that sends notifications
     * @return A map with the output of the notification
     * @see NotificationType
     */
    public static HashMap<String, String> getNotification(final NotificationType notificationType,
                                                          final String notifier) {
        HashMap<String, String> notification;
        notification = new HashMap<>();
        notification.put("name", "New " + notificationType);
        notification.put("description", "New " + notificationType + " from " + notifier + ".");
        return notification;
    }
}
