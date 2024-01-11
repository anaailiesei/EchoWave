package managers.normalUser;

import commands.CommandType;
import commands.normalUser.general.SwitchConnectionStatus;
import fileio.input.CommandInput;
import fileio.output.Output;
import managers.commands.CommandHandler;

public final class ConnectionStatusManager implements CommandHandler {
    private static ConnectionStatusManager instance;

    private ConnectionStatusManager() {
    }

    /**
     * Gets the instance for this class
     *
     * @return the instance
     */
    public static synchronized ConnectionStatusManager getInstance() {
        if (instance == null) {
            instance = new ConnectionStatusManager();
        }
        return instance;
    }

    /**
     * Performs the switch connection status command, toggling the connection status of a user.
     * This method executes the necessary operation to switch the connection status (online/offline)
     * of the user identified by the provided username.
     *
     * @param command The input command containing the username for which to switch
     *                the connection status.
     * @return An Output object with the result of the operation.
     */
    public static Output performSwitchConnectionStatus(final CommandInput command) {
        String username = command.getUsername();
        SwitchConnectionStatus.execute(username);
        String message = SwitchConnectionStatus.toString(username);
        return new Output(command, message);
    }

    @Override
    public Output performCommand(final CommandInput command) {
        CommandType type = command.getCommand();
        return switch (type) {
            case switchConnectionStatus -> performSwitchConnectionStatus(command);
            default -> throw new IllegalStateException("Unexpected command for "
                    + this.getClass().getSimpleName() + ": " + type);
        };
    }
}
