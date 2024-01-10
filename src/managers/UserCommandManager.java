package managers;

import commands.CommandType;
import fileio.input.CommandInput;
import fileio.output.Output;
import libraries.users.UsersLibrariesStats;
import managers.commands.CommandHandler;
import entities.user.User;

public final class UserCommandManager implements CommandHandler {
    private static UserCommandManager instance;

    private UserCommandManager() {
    }

    /**
     * Get the instance for this class
     *
     * @return the instance
     */
    public static synchronized UserCommandManager getInstance() {
        if (instance == null) {
            instance = new UserCommandManager();
        }
        return instance;
    }

    /**
     * Performs the wrapped command
     *
     * @param command the command that specifies the wrapped command parameters
     * @return an {@code Output} object with the command and result
     * (with top 5 listens for each category based on entities.user)
     */
    public static Output performWrapped(final CommandInput command) {
        User user = UsersLibrariesStats.getUserByName(command.getUsername());
        if (user.noStats()) {
            String message = "No data to show for entities.user " + command.getUsername() + ".";
            return new Output(command, message);
        }
        Object result = user.wrapped();
        return new Output(command, result);
    }

    @Override
    public Output performCommand(final CommandInput command) {
        CommandType commandType = command.getCommand();

        return switch (commandType) {
            case wrapped -> performWrapped(command);
            default -> throw new IllegalStateException("Unexpected command for "
                    + this.getClass().getSimpleName() + ": " + commandType);
        };
    }
}
