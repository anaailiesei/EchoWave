package commands.normalUser.pageNavigation;

import entities.user.NormalUser;
import libraries.users.NormalUsersLibrary;

public final class ChangePage {
    private static final String HOME = "Home";
    private static final String LIKED = "LikedContent";
    private static final String ARTIST = "Artist";
    private static final String HOST = "Host";
    private static State state;
    private ChangePage() {
    }

    /**
     * Execute the change page command
     *
     * @param username The user that wants to change the page
     * @param pageName     The page to be changed
     */
    public static void execute(final String username, final String pageName) {
        checkConditions(pageName);
        if (state.equals(State.noPage)) {
            return;
        }
        NormalUser user = NormalUsersLibrary.getInstance().getUserByName(username);
        assert user != null;
        PageChangeInvoker invoker = user.getPageChangeInvoker();
        ChangePageUser changePage = new ChangePageUser(user, pageName);
        invoker.executeOperation(changePage);
        invoker.clearRedoHistory();
    }

    /**
     * Checks the conditions for performing this operation
     * For a successful operation, the page should be either "home" or the "liked page"
     *
     * @param page The page to be checked
     */
    private static void checkConditions(final String page) {
        if (page.equals(HOME)
                || page.equals(LIKED)
                || page.equals(HOST)
                || page.equals(ARTIST)) {
            state = State.successfullyChanged;
        } else {
            state = State.noPage;
        }
    }

    /**
     * Function for the output message of this command
     *
     * @param username the name of the entities.user that performs this command
     * @param page     The page they switched on
     * @return A string with the message
     */
    public static String toString(final String username, final String page) {
        if (state.equals(State.noPage)) {
            return username + " is trying to access a non-existent page.";
        }
        return username + " accessed " + page + " successfully.";
    }

    private enum State {
        noPage, successfullyChanged
    }
}
