package commands.host;

import libraries.users.HostsLibrary;
import libraries.users.UsersLibrariesStats;
import profile.host.Announcement;
import entities.user.Host;

public final class AddAnnouncement {
    private static State state;

    private AddAnnouncement() {
    }

    /**
     * Execute the add announcement command
     *
     * @param announcementName The name of the announcement to be added
     * @param username         The name for the entities.user that wants to add an announcement
     * @param description      The description of the announcement
     */
    public static void execute(final String announcementName,
                               final String username,
                               final String description) {
        Host host = HostsLibrary.getInstance().getHostByName(username);
        checkConditions(announcementName, username, host);
        if (!state.equals(State.successfullyAddedAnnouncement)) {
            return;
        }
        Announcement announcement = new Announcement(announcementName, description);
        host.addAnnouncement(announcement);
    }

    /**
     * Checks the conditions for performing this operation
     * The announcement can be successfully added if the given username exists,
     * if it's a host and if the announcement with the given name doesn't exist already
     *
     * @param announcementName The name of the announcement to be checked
     * @param username         The name of the entities.user to be checked if it exists
     * @param host             The host (if it doesn't exist, this is null)
     */
    private static void checkConditions(final String announcementName,
                                        final String username,
                                        final Host host) {
        if (!UsersLibrariesStats.userExists(username)) {
            state = State.noUser;
        } else if (host == null) {
            state = State.notHost;
        } else if (host.announcementExists(announcementName)) {
            state = State.duplicatedAnnouncement;
        } else {
            state = State.successfullyAddedAnnouncement;
        }
    }

    /**
     * Function for the output message of this command
     *
     * @param username The username entities.user that adds the announcement
     * @return A string with the message
     */
    public static String toString(final String username) {
        return switch (state) {
            case noUser -> "The username " + username + " doesn't exist.";
            case successfullyAddedAnnouncement -> username
                    + " has successfully added new announcement.";
            case notHost -> username + " is not a host.";
            case duplicatedAnnouncement -> username
                    + " has already added an announcement with this name.";
        };
    }

    private enum State {
        noUser, successfullyAddedAnnouncement, notHost, duplicatedAnnouncement
    }
}
