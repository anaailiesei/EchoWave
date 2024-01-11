package managers.normalUser;

import commands.CommandType;
import commands.normalUser.general.ChangePage;
import commands.normalUser.general.Subscribe;
import fileio.input.CommandInput;
import fileio.output.Output;
import managers.commands.CommandHandler;

public final class PageSystemManager implements CommandHandler {
    private static PageSystemManager instance;

    private PageSystemManager() {
    }

    /**
     * Gets the instance fo this class
     *
     * @return the instance
     */
    public static synchronized PageSystemManager getInstance() {
        if (instance == null) {
            instance = new PageSystemManager();
        }
        return instance;
    }

    /**
     * Performs the change page command
     *
     * @param command the command that specifies the change page command parameters
     * @return an {@code Output} object with the command and message
     * @see commands.normalUser.general.ChangePage
     */
    public static Output performChangePage(final CommandInput command) {
        ChangePage.execute(command.getUsername(), command.getNextPage());
        String message = ChangePage.toString(command.getUsername(), command.getNextPage());
        return new Output(command, message);
    }
    /**
     * Performs the subscribe command
     * If user wasn't subscribed to current page's owner, they subscribe to that artist/host
     * Else they unsubscribe
     *
     * @param command the command that specifies the subscribe command parameters
     * @return an {@code Output} object with the command and message
     * @see commands.normalUser.general.Subscribe
     */
    public static Output performSubscribe(final CommandInput command) {
        Subscribe.execute(command.getUsername());
        String message = Subscribe.toString(command.getUsername());
        return new Output(command, message);
    }

    @Override
    public Output performCommand(final CommandInput command) {
        CommandType type = command.getCommand();
        return switch (type) {
            case changePage -> performChangePage(command);
            case subscribe -> performSubscribe(command);
            default -> throw new IllegalStateException("Unexpected command for "
                    + this.getClass().getSimpleName() + ": " + type);
        };
    }
}
