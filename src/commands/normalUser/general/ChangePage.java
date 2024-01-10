package commands.normalUser.general;

import libraries.users.ArtistsLibrary;
import libraries.users.HostsLibrary;
import libraries.users.NormalUsersLibrary;
import managers.normalUser.AppManager;
import user.Artist;
import user.Host;
import user.NormalUser;

public final class ChangePage {
    private static final String HOME = "Home";
    private static final String LIKED = "LikedContent";
    private static State state;
    private ChangePage() {
    }

    /**
     * Execute the change page command
     *
     * @param username The user that wants to change the page
     * @param page     The page to be changed
     */
    public static void execute(final String username, final String page) {
        checkConditions(page);
        if (state.equals(State.noPage)) {
            return;
        }
        NormalUser user = NormalUsersLibrary.getInstance().getUserByName(username);
        assert user != null;
        AppManager app = user.getApp();
        String owner = app.getPageOwner();
        if (app.getPage().equals(AppManager.Page.hostPage)) {
            Host host = HostsLibrary.getInstance().getHostByName(owner);
            host.decrementPageViewersCount();
        } else if (app.getPage().equals(AppManager.Page.artistPage)) {
            Artist artist = ArtistsLibrary.getInstance().getArtistByName(owner);
            artist.decrementPageViewersCount();
        }
        if (page.equals(HOME)) {
            app.setPage(AppManager.Page.homePage);
        } else {
            app.setPage(AppManager.Page.likedContentPage);
        }
    }

    /**
     * Checks the conditions for performing this operation
     * For a successful operation, the page should be either "home" or the "liked page"
     *
     * @param page The page to be checked
     */
    private static void checkConditions(final String page) {
        if (page.equals(HOME) || page.equals(LIKED)) {
            state = State.successfullyChanged;
        } else {
            state = State.noPage;
        }
    }

    /**
     * Function for the output message of this command
     *
     * @param username the name of the user that performs this command
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
