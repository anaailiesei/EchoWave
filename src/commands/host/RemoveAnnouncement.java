package commands.host;

import libraries.users.HostsLibrary;
import libraries.users.UsersLibrariesStats;
import user.Host;

public final class RemoveAnnouncement {
    private static State state;

    private RemoveAnnouncement() {
    }

    /**
     * Execute the remove announcement command
     *
     * @param announcementName The name of the announcement to be removed
     * @param username         The name of the user that wants to delete an announcement
     */
    public static void execute(final String announcementName,
                               final String username) {
        Host host = HostsLibrary.getInstance().getHostByName(username);
        checkConditions(announcementName, username, host);
        if (!state.equals(State.successfullyRemovedAnnouncement)) {
            return;
        }
        host.removeAnnouncement(announcementName);
    }

    /**
     * Checks the conditions for performing this operation
     * For a successful operation, the user should exist, and they should be a host,
     * and the announcement should exist
     *
     * @param announcementName The name of the announcement
     * @param username         The name of the user that wants to delete the announcement
     * @param host             The host (this should be non-null for a successful operation)
     */
    private static void checkConditions(final String announcementName,
                                        final String username,
                                        final Host host) {
        if (!UsersLibrariesStats.userExists(username)) {
            state = State.noUser;
        } else if (host == null) {
            state = State.notHost;
        } else if (!host.announcementExists(announcementName)) {
            state = State.noAnnouncement;
        } else {
            state = State.successfullyRemovedAnnouncement;
        }
    }

    /**
     * Function for the output message of this command based on the state variable
     *
     * @param username The name of the user that performs the operation
     * @return A string with the message
     */
    public static String toString(final String username) {
        return switch (state) {
            case noUser -> "The username " + username + " doesn't exist.";
            case successfullyRemovedAnnouncement -> username
                    + " has successfully deleted the announcement.";
            case notHost -> username + " is not a host.";
            case noAnnouncement -> username + " has no announcement with the given name.";
        };
    }

    private enum State {
        noUser, successfullyRemovedAnnouncement, notHost, noAnnouncement
    }
}
