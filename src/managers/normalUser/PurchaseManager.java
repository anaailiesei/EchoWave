package managers.normalUser;

import commands.CommandType;
import commands.normalUser.general.BuyMerch;
import entities.user.NormalUser;
import fileio.input.CommandInput;
import fileio.output.Output;
import libraries.users.NormalUsersLibrary;
import managers.commands.CommandHandler;

public final class PurchaseManager implements CommandHandler {
    private static PurchaseManager instance;

    private PurchaseManager() {
    }

    /**
     * Gets the instance for this class
     *
     * @return the instance
     */
    public static synchronized PurchaseManager getInstance() {
        if (instance == null) {
            instance = new PurchaseManager();
        }
        return instance;
    }

    /**
     * Performs the buy merch command
     * After this, merch is also added in the artist's merch revenue
     *
     * @param command The input command containing the username for the user that
     *                buys merch
     * @return An Output object with the result of the operation.
     */
    public static Output performBuyMerch(final CommandInput command) {
        String username = command.getUsername();
        String merchName = command.getName();
        BuyMerch.execute(username, merchName);
        String message = BuyMerch.toString(username, merchName);
        return new Output(command, message);
    }

    /**
     * Performs the see merch command
     *
     * @param command The input command containing the username for the user
     * @return An Output object with the result of the operation.
     */
    public static Output performSeeMerch(final CommandInput command) {
        String username = command.getUsername();
        NormalUser user = NormalUsersLibrary.getInstance().getUserByName(username);
        if (user == null) {
            return new Output(command, "The username " + username + " doesn't exist.");
        }
        return new Output(command, user.getMerchandise());
    }

    @Override
    public Output performCommand(final CommandInput command) {
        CommandType type = command.getCommand();
        return switch (type) {
            case buyMerch -> performBuyMerch(command);
            case seeMerch -> performSeeMerch(command);
            default -> throw new IllegalStateException("Unexpected command for "
                    + this.getClass().getSimpleName() + ": " + type);
        };
    }
}
