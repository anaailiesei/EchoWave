package commands.admin;

import entities.user.NormalUser;
import libraries.users.NormalUsersLibrary;
import statistics.calculator.NormalUserCalculateRevenue;
import statistics.calculator.RevenueCalculator;

public final class RemovePremiumUser {
    private static State state;

    private RemovePremiumUser() {
    }

    /**
     * Execute the command for adding a premium user
     *
     * @param username The username of the user that should be added
     */
    public static void execute(final String username) {
        NormalUser user = NormalUsersLibrary.getInstance().getUserByName(username);
        checkConditions(user);
        if (!state.equals(State.removedPremium)) {
            return;
        }
        assert user != null;
        user.setPremium(false);
        RevenueCalculator calculator = new RevenueCalculator();
        calculator.calculateRevenue(new NormalUserCalculateRevenue(user));
        user.getApp().getListenTracker().emptyPremiumSongs();
    }

    /**
     * Check the conditions for performing this operation
     * User should already exist, and they shouldn't have bought a subscription
     * yet for a successful operation.
     *
     * @param user The premium user that should be added
     */
    private static void checkConditions(final NormalUser user) {
        if (user == null) {
            state = State.noUser;
        } else if (!user.isPremium()) {
            state = State.notPremium;
        } else {
            state = State.removedPremium;
        }
    }

    /**
     * Function for the output message of this command
     *
     * @param username The username of the added premium user
     * @return A string with the message
     */
    public static String toString(final String username) {
        if (state.equals(State.noUser)) {
            return "The username " + username + " doesn't exist.";
        } else if (state.equals(State.notPremium)) {
            return username + " is not a premium user.";
        }
        return username + " cancelled the subscription successfully.";
    }

    private enum State {
        noUser, notPremium, removedPremium
    }
}
