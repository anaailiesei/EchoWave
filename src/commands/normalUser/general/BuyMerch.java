package commands.normalUser.general;

import commands.normalUser.pageNavigation.PageType;
import entities.user.Artist;
import entities.user.NormalUser;
import libraries.users.ArtistsLibrary;
import libraries.users.NormalUsersLibrary;
import managers.normalUser.AppManager;

public final class BuyMerch {
    private static State state;
    private static Artist artist;
    private BuyMerch() {
    }

    /**
     * Execute the buyMerch command
     * This also adds the revenue made from merch to the artist's account
     *
     * @param username The user that wants to buy merch
     * @param merchName The merch name the user wants to buy
     */
    public static void execute(final String username, final String merchName) {
        NormalUser user = NormalUsersLibrary.getInstance().getUserByName(username);
        checkConditions(user, merchName);
        if (!state.equals(State.boughtMerch)) {
            return;
        }

        assert user != null;
        artist.addMerchRevenue(merchName);
        user.addMerch(merchName);
    }

    /**
     * Checks the conditions for performing this operation
     * For a successful operation, the user should exist, and
     * the page they're on should be either "artist"
     * This also sets the artist the merch is owned by
     *
     * @param user The user for which we check the conditions
     */
    private static void checkConditions(final NormalUser user, final String merchName) {
        if (user == null) {
            state = State.noUser;
            return;
        }
        AppManager app = user.getApp();
        PageType page = app.getPage().pageType();
        if (!page.equals(PageType.artistPage)) {
            state = State.noPage;
            return;
        }
        artist = ArtistsLibrary.getInstance().getArtistByName(app.getPageOwner());
        if (!artist.merchExists(merchName)) {
            state = State.noMerch;
        } else {
            state = State.boughtMerch;
        }
    }

    /**
     * Function for the output message of this command
     *
     * @param username the name of the user that performs this command
     * @param merchName the name of the merch to be bought by the user
     * @return A string with the message
     */
    public static String toString(final String username, final String merchName) {
        return switch (state) {
            case noUser -> "The username " + username + " doesn't exist.";
            case noPage -> "Cannot buy merch from this page.";
            case noMerch -> "The merch " + merchName + " doesn't exist.";
            case boughtMerch -> username + " has added new merch successfully.";
        };
    }

    private enum State {
        noPage, noUser, noMerch, boughtMerch
    }
}
