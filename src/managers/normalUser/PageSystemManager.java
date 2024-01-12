package managers.normalUser;

import commands.CommandType;
import commands.normalUser.general.Subscribe;
import commands.normalUser.pageNavigation.ChangePage;
import commands.normalUser.pageNavigation.PageChangeInvoker;
import entities.audio.Song;
import entities.audio.collections.Playlist;
import entities.user.NormalUser;
import fileio.input.CommandInput;
import fileio.output.Output;
import libraries.users.NormalUsersLibrary;
import managers.commands.CommandHandler;
import recommendation.Recommendation;

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
     * @see ChangePage
     */
    public static Output performChangePage(final CommandInput command) {
        String username = command.getUsername();
        ChangePage.execute(username, command.getNextPage());
        String message = ChangePage.toString(command.getUsername(), command.getNextPage());
        NormalUser user = NormalUsersLibrary.getInstance().getUserByName(username);
        if (user != null) {
            user.getPageChangeInvoker().clearRedoHistory();
        }
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

    /**
     * Performs the next page operation
     * If user can't go forward anymore nothing is done
     *
     * @param command the command that specifies the next page command parameters
     * @return an {@code Output} object with the command and message
     * @see commands.normalUser.pageNavigation.ChangePageUser
     */
    public static Output performNextPage(final CommandInput command) {
        String username = command.getUsername();
        NormalUser user = NormalUsersLibrary.getInstance().getUserByName(username);
        assert user != null;
        PageChangeInvoker invoker = user.getPageChangeInvoker();
        String message = invoker.redoOperation();
        if (message == null) {
            message = "There are no pages left to go forward.";
        }
        return new Output(command, message);
    }

    /**
     * Performs the previous page operation
     * If user can't go backward anymore nothing is done
     *
     * @param command the command that specifies the previous page command parameters
     * @return an {@code Output} object with the command and message
     * @see commands.normalUser.pageNavigation.ChangePageUser
     */
    public static Output performPreviousPage(final CommandInput command) {
        String username = command.getUsername();
        NormalUser user = NormalUsersLibrary.getInstance().getUserByName(username);
        assert user != null;
        PageChangeInvoker invoker = user.getPageChangeInvoker();
        String message = invoker.undoOperation();
        if (message == null) {
            message = "There are no pages left to go back.";
        }
        return new Output(command, message);
    }

    /**
     * Performs update recommendations
     *
     * @param command The input command for printing the current page.
     * @return An Output containing the result of the print operation.
     */
    public Output performUpdateRecommendations(final CommandInput command) {
        // TODO: delete this
//        if (!app.isOnline()) {
//            return new PageOutput(command, app.getUserOfflineMessage());
//        }
        String username = command.getUsername();
        NormalUser user = NormalUsersLibrary.getInstance().getUserByName(username);
        String recommendationType = command.getRecommendationType();
        String message;
        if (user == null) {
            message = "wtf";
            return new Output(command, message);
        }
        switch (recommendationType) {
            case "fans_playlist" -> {
                Playlist playlist = Recommendation.fansRecommendations(user);
                user.addRecommendedPlaylist(playlist);
            }
            case "random_playlist" -> {
                Playlist playlist = Recommendation.randomPlaylist(user);
                user.addRecommendedPlaylist(playlist);
            }
            case "random_song" -> {
                Song song = Recommendation.randomSong(user);
                if (song != null) {
                    user.addRecommendedSong(song);
                }
            }
            default -> {
            }
        }

        message = "The recommendations for user "
                + username
                + " have been updated successfully.";

        return new Output(command, message);
    }


    @Override
    public Output performCommand(final CommandInput command) {
        CommandType type = command.getCommand();
        return switch (type) {
            case changePage -> performChangePage(command);
            case subscribe -> performSubscribe(command);
            case nextPage -> performNextPage(command);
            case previousPage -> performPreviousPage(command);
            case updateRecommendations -> performUpdateRecommendations(command);
            default -> throw new IllegalStateException("Unexpected command for "
                    + this.getClass().getSimpleName() + ": " + type);
        };
    }
}
