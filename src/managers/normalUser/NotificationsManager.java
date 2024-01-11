package managers.normalUser;

import commands.CommandType;
import entities.user.NormalUser;
import fileio.input.CommandInput;
import fileio.output.Output;
import libraries.users.NormalUsersLibrary;
import managers.commands.CommandHandler;

import java.util.ArrayList;
import java.util.HashMap;

public final class NotificationsManager implements CommandHandler {
    private static NotificationsManager instance;

    private NotificationsManager() {
    }

    /**
     * Gets the instance for this class
     *
     * @return the instance
     */
    public static synchronized NotificationsManager getInstance() {
        if (instance == null) {
            instance = new NotificationsManager();
        }
        return instance;
    }

    /**
     * Performs the switch connection get notifications command
     * After this, notifications for the user are deleted
     *
     * @param command The input command containing the username for which to
     *                get the notifications
     * @return An Output object with the result of the operation.
     */
    public static Output performGetNotifications(final CommandInput command) {
        String username = command.getUsername();
        NormalUser user = NormalUsersLibrary.getInstance().getUserByName(username);
        ArrayList<HashMap<String, String>> notifications;
        if (user == null) {
            notifications = new ArrayList<>();
        } else {
            notifications = user.getNotifications();
        }
        return new Output(command, notifications, 0);
    }

    @Override
    public Output performCommand(final CommandInput command) {
        CommandType type = command.getCommand();
        return switch (type) {
            case getNotifications -> performGetNotifications(command);
            default -> throw new IllegalStateException("Unexpected command for "
                    + this.getClass().getSimpleName() + ": " + type);
        };
    }
}
