package commands.host;

import entities.audio.collections.Podcast;
import libraries.audio.PodcastsLibrary;
import libraries.users.HostsLibrary;
import libraries.users.UsersLibrariesStats;
import entities.user.Host;

public final class RemovePodcast {
    private static State state;

    private RemovePodcast() {
    }

    /**
     * Execute the remove announcement command
     *
     * @param podcastName The name of the podcast to be deleted
     * @param username    The name of th entities.user that wants to delete the podcast
     */
    public static void execute(final String podcastName,
                               final String username) {
        Host host = HostsLibrary.getInstance().getHostByName(username);
        checkConditions(podcastName, username, host);
        if (!state.equals(State.successfullyRemovedPodcast)) {
            return;
        }
        Podcast podcast = host.getPodcastByName(podcastName);
        PodcastsLibrary.getInstance().removeItem(podcast);
        host.removePodcast(podcast);
    }

    /**
     * Checks the conditions for performing this operation
     * For a successful operation, the entities.user should exist, and they should be a host,
     * the podcast should exist, and it should be deletable
     *
     * @param podcastName The name of the podcast to be checked
     * @param username    The name of the entities.user that wants to add the podcast
     * @param host        The host (this should be a non-null reference for a successful operation)
     */
    private static void checkConditions(final String podcastName,
                                        final String username,
                                        final Host host) {
        if (!UsersLibrariesStats.userExists(username)) {
            state = State.noUser;
        } else if (host == null) {
            state = State.notHost;
        } else if (!host.podcastExists(podcastName)) {
            state = State.noPodcast;
        } else if (!host.getPodcastByName(podcastName).isDeletable()) {
            state = State.notDeletable;
        } else {
            state = State.successfullyRemovedPodcast;
        }
    }

    /**
     * Function for the output message of this command based on the state variable
     *
     * @param username The name of the entities.user that performs the operation
     * @return A string with the message
     */
    public static String toString(final String username) {
        return switch (state) {
            case noUser -> "The username " + username + " doesn't exist.";
            case noPodcast -> username + " doesn't have a podcast with the given name.";
            case notDeletable -> username + " can't delete this podcast.";
            default -> username + " deleted the podcast successfully.";
        };
    }

    private enum State {
        noUser, successfullyRemovedPodcast, notHost, noPodcast, notDeletable
    }
}
